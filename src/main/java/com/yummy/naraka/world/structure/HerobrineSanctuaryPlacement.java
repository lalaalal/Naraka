package com.yummy.naraka.world.structure;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.*;

public class HerobrineSanctuaryPlacement {
    public static final PiecePositionProvider POSITION_PROVIDER = new HerobrineSanctuaryPlacement.PiecePositionProvider(
            "herobrine_sanctuary",
            List.of(0, 1, 2, 3), List.of(30, 1, -28, -76),
            List.of(0, 1, 2), List.of(1, 49, 97),
            List.of(1, 2, 3, 4, 5, 6, 7, 8), List.of(26, 74, 122, 170, 90, 138, 110, 158)
    );
    public static final List<Vec3i> PIECE_POSITIONS = fromString(
            "005", "006", "015", "016",
            "101", "102", "103", "104", "111", "112", "113", "114", "127", "128",
            "201", "202", "203", "204", "211", "212", "213", "214", "227", "228",
            "305", "306", "315", "316"
    );

    public static final ResourceLocation ENTRANCE = NarakaMod.location("herobrine_sanctuary_entrance");
    public static final BlockPos ENTRANCE_POS = new BlockPos(-4, -7, 0);

    public static Optional<Structure.GenerationStub> addPieces(Structure.GenerationContext context, BlockPos basePos, BlockPos offset) {
        final BlockPos structurePos = basePos.offset(offset);
        StructureTemplateManager templateManager = context.structureTemplateManager();
        return Optional.of(new Structure.GenerationStub(structurePos, builder -> {
            builder.addPiece(new HerobrineSanctuaryPiece(templateManager, ENTRANCE, structurePos.offset(ENTRANCE_POS)));
            for (Vec3i piecePosition : PIECE_POSITIONS) {
                ResourceLocation location = POSITION_PROVIDER.getPieceLocation(piecePosition);
                BlockPos actualPosition = structurePos.offset(POSITION_PROVIDER.getPiecePosition(piecePosition));
                builder.addPiece(new HerobrineSanctuaryPiece(templateManager, location, actualPosition));
            }
        }));
    }

    public static List<Vec3i> fromString(String... positions) {
        List<Vec3i> list = new ArrayList<>();
        for (String position : positions) {
            if (position.length() != 3)
                throw new IllegalStateException();
            int x = Integer.parseInt(position.substring(0, 1));
            int y = Integer.parseInt(position.substring(1, 2));
            int z = Integer.parseInt(position.substring(2, 3));
            list.add(new Vec3i(x, y, z));
        }
        return list;
    }

    public static class PiecePositionProvider {
        private final String structureName;
        private final Map<Integer, Integer> xMapping;
        private final Map<Integer, Integer> yMapping;
        private final Map<Integer, Integer> zMapping;

        private static Map<Integer, Integer> createMapping(List<Integer> targets, List<Integer> positions) {
            if (targets.size() != positions.size())
                throw new IllegalStateException();
            HashMap<Integer, Integer> map = new HashMap<>();
            for (int index = 0; index < targets.size(); index++) {
                if (map.put(targets.get(index), positions.get(index)) != null)
                    throw new IllegalStateException();
            }

            return map;
        }

        public PiecePositionProvider(
                String structureName,
                List<Integer> xTargets, List<Integer> xPositions,
                List<Integer> yTargets, List<Integer> yPositions,
                List<Integer> zTargets, List<Integer> zPositions
        ) {
            this.structureName = structureName;
            xMapping = createMapping(xTargets, xPositions);
            yMapping = createMapping(yTargets, yPositions);
            zMapping = createMapping(zTargets, zPositions);
        }

        public PiecePositionProvider(String structureName, Map<Integer, Integer> xMapping, Map<Integer, Integer> yMapping, Map<Integer, Integer> zMapping) {
            this.structureName = structureName;
            this.xMapping = xMapping;
            this.yMapping = yMapping;
            this.zMapping = zMapping;
        }

        public ResourceLocation getPieceLocation(int pieceX, int pieceY, int pieceZ) {
            return NarakaMod.location(structureName + "_" + pieceX + pieceY + pieceZ);
        }

        public ResourceLocation getPieceLocation(Vec3i piecePosition) {
            return getPieceLocation(piecePosition.getX(), piecePosition.getY(), piecePosition.getZ());
        }

        public BlockPos getPiecePosition(int pieceX, int pieceY, int pieceZ) {
            int x = xMapping.get(pieceX);
            int y = yMapping.get(pieceY);
            int z = zMapping.get(pieceZ);

            return new BlockPos(x, y, z);
        }

        public BlockPos getPiecePosition(Vec3i piecePosition) {
            return getPiecePosition(piecePosition.getX(), piecePosition.getY(), piecePosition.getZ());
        }
    }
}

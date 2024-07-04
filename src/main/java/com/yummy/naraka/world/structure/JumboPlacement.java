package com.yummy.naraka.world.structure;

import com.mojang.datafixers.util.Pair;
import com.yummy.naraka.NarakaMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.*;

public class JumboPlacement {
    public static Optional<Structure.GenerationStub> addPieces(
            Structure.GenerationContext context,
            PiecePositionProvider positionProvider,
            List<Vec3i> piecePositions,
            List<Pair<Holder<PiecePlacement>, BlockPos>> customPiecePlacements,
            BlockPos basePos
    ) {
        StructureTemplateManager templateManager = context.structureTemplateManager();
        return Optional.of(new Structure.GenerationStub(basePos, builder -> {
            for (Pair<Holder<PiecePlacement>, BlockPos> pair : customPiecePlacements) {
                PiecePlacement piecePlacement = pair.getFirst().value();
                piecePlacement.place(context, builder, basePos, pair.getSecond());
            }
            for (Vec3i piecePosition : piecePositions) {
                ResourceLocation location = positionProvider.getPieceLocation(piecePosition);
                BlockPos actualPosition = basePos.offset(positionProvider.getPiecePosition(piecePosition));
                builder.addPiece(new JumboPiece(templateManager, location, actualPosition));
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

    public static List<Vec3i> simple(int xCount, int yCount, int zCount) {
        List<Vec3i> list = new ArrayList<>();
        for (int x = 0; x < xCount; x++) {
            for (int y = 0; y < yCount; y++) {
                for (int z = 0; z < zCount; z++) {
                    list.add(new Vec3i(x, y, z));
                }
            }
        }

        return list;
    }

    public interface PiecePositionProvider {
        String name();

        default ResourceLocation getPieceLocation(int pieceX, int pieceY, int pieceZ) {
            return NarakaMod.location(name() + "/" + pieceX + pieceY + pieceZ);
        }

        default ResourceLocation getPieceLocation(Vec3i piecePosition) {
            return getPieceLocation(piecePosition.getX(), piecePosition.getY(), piecePosition.getZ());
        }

        BlockPos getPiecePosition(int pieceX, int pieceY, int pieceZ);

        default BlockPos getPiecePosition(Vec3i piecePosition) {
            return getPiecePosition(piecePosition.getX(), piecePosition.getY(), piecePosition.getZ());
        }
    }

    public record SimplePiecePositionProvider(String name) implements PiecePositionProvider {
        public static final int TEMPLATE_SIZE = 48;

        @Override
        public BlockPos getPiecePosition(int pieceX, int pieceY, int pieceZ) {
            int x = pieceX * TEMPLATE_SIZE;
            int y = pieceY * TEMPLATE_SIZE;
            int z = pieceZ * TEMPLATE_SIZE;
            return new BlockPos(x, y, z);
        }
    }

    public static class ComplexPiecePositionProvider implements PiecePositionProvider {
        private final String name;
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

        public ComplexPiecePositionProvider(
                String name,
                List<Integer> xTargets, List<Integer> xPositions,
                List<Integer> yTargets, List<Integer> yPositions,
                List<Integer> zTargets, List<Integer> zPositions
        ) {
            this.name = name;
            xMapping = createMapping(xTargets, xPositions);
            yMapping = createMapping(yTargets, yPositions);
            zMapping = createMapping(zTargets, zPositions);
        }

        public ComplexPiecePositionProvider(String name, Map<Integer, Integer> xMapping, Map<Integer, Integer> yMapping, Map<Integer, Integer> zMapping) {
            this.name = name;
            this.xMapping = xMapping;
            this.yMapping = yMapping;
            this.zMapping = zMapping;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public BlockPos getPiecePosition(int pieceX, int pieceY, int pieceZ) {
            int x = xMapping.get(pieceX);
            int y = yMapping.get(pieceY);
            int z = zMapping.get(pieceZ);

            return new BlockPos(x, y, z);
        }
    }
}

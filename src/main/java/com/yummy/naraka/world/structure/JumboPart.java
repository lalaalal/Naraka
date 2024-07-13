package com.yummy.naraka.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.world.structure.piece.PiecePositionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

import java.util.ArrayList;
import java.util.List;

public record JumboPart(String name, int xCount, int yCount, int zCount,
                        BlockPos offset) implements PiecePositionProvider {
    public static final MapCodec<JumboPart> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("part_name").forGetter(JumboPart::name),
                    Codec.INT.listOf().fieldOf("piece_count").forGetter(JumboPart::pieceCount),
                    BlockPos.CODEC.fieldOf("offset").forGetter(JumboPart::offset)
            ).apply(instance, JumboPart::new)
    );

    public static final int TEMPLATE_SIZE = 48;

    private static List<Vec3i> createPositions(int xCount, int yCount, int zCount) {
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

    private JumboPart(String name, List<Integer> pieceCounts, BlockPos offset) {
        this(name, pieceCounts.get(0), pieceCounts.get(1), pieceCounts.get(2), offset);
    }

    private List<Integer> pieceCount() {
        return List.of(xCount, yCount, zCount);
    }

    @Override
    public BlockPos getPiecePosition(int pieceX, int pieceY, int pieceZ) {
        int x = pieceX * TEMPLATE_SIZE;
        int y = pieceY * TEMPLATE_SIZE;
        int z = pieceZ * TEMPLATE_SIZE;
        return new BlockPos(x, y, z).offset(offset);
    }

    public List<Vec3i> getPositions() {
        return createPositions(xCount, yCount, zCount);
    }
}

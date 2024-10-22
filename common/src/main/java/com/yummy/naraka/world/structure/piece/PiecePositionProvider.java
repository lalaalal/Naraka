package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;

public interface PiecePositionProvider {
    String name();

    default ResourceLocation getPieceLocation(int pieceX, int pieceY, int pieceZ) {
        return NarakaMod.location(name() + "/" + pieceX + pieceY + pieceZ);
    }

    default ResourceLocation getPieceLocation(String structureName, int pieceX, int pieceY, int pieceZ) {
        return NarakaMod.location(structureName + "/" + name() + "-" + pieceX + pieceY + pieceZ);
    }

    default ResourceLocation getPieceLocation(Vec3i piecePosition) {
        return getPieceLocation(piecePosition.getX(), piecePosition.getY(), piecePosition.getZ());
    }

    default ResourceLocation getPieceLocation(String structureName, Vec3i piecePosition) {
        return getPieceLocation(structureName, piecePosition.getX(), piecePosition.getY(), piecePosition.getZ());
    }

    BlockPos getPiecePosition(int pieceX, int pieceY, int pieceZ);

    default BlockPos getPiecePosition(Vec3i piecePosition) {
        return getPiecePosition(piecePosition.getX(), piecePosition.getY(), piecePosition.getZ());
    }
}

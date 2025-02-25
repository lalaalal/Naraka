package com.yummy.naraka.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class NarakaUtils {
    public interface PositionConsumer {
        void accept(int x, int y, int z);
    }

    public static void sphere(BoundingBox box, float size, PositionConsumer consumer) {
        float xRadius = (float) (box.maxX() - box.minX() + 1) / 2;
        float zRadius = (float) (box.maxZ() - box.minZ() + 1) / 2;
        float yRadius = (float) (box.maxY() - box.minZ() + 1) / 2;
        float centerX = box.minX() + xRadius;
        float centerZ = box.minZ() + zRadius;
        float centerY = box.minY() + yRadius;

        for (int y = box.minY(); y <= box.maxY(); y++) {
            float yRatio = (y - centerY) / yRadius;
            for (int x = box.minX(); x <= box.maxX(); x++) {
                float xRatio = (x - centerX) / xRadius;
                for (int z = box.minZ(); z <= box.maxZ(); z++) {
                    float zRatio = (z - centerZ) / zRadius;
                    if (xRatio * xRatio + yRatio * yRatio + zRatio * zRatio <= 1.05f * size)
                        consumer.accept(x, y, z);
                }
            }
        }
    }

    public static void sphere(BoundingBox box, float size, Consumer<BlockPos> consumer) {
        sphere(box, size, (x, y, z) -> consumer.accept(new BlockPos(x, y, z)));
    }

    public static boolean isInSphere(BoundingBox box, float size, int x, int y, int z) {
        return isInSphere(box, size, new Vec3i(x, y, z));
    }

    public static boolean isInSphere(BoundingBox box, float size, Vec3i pos) {
        float xRadius = (float) (box.maxX() - box.minX() + 1) / 2;
        float zRadius = (float) (box.maxZ() - box.minZ() + 1) / 2;
        float yRadius = (float) (box.maxY() - box.minZ() + 1) / 2;
        float centerX = box.minX() + xRadius;
        float centerZ = box.minZ() + zRadius;
        float centerY = box.minY() + yRadius;
        float xRatio = (pos.getX() - centerX) / xRadius;
        float zRatio = (pos.getZ() - centerZ) / zRadius;
        float yRatio = (pos.getY() - centerY) / yRadius;

        return xRatio * xRatio + yRatio * yRatio + zRatio * zRatio <= 1.05f * size;
    }

    public static Vec3 vec3(Vec3i pos) {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockPos pos(Vec3 pos) {
        return new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);
    }

    public static BlockPos findFaceSturdy(LevelAccessor level, BlockPos from, Direction faceDirection) {
        BlockPos.MutableBlockPos current = from.mutable();
        while (-60 < current.getY() && current.getY() < 200) {
            BlockPos pos = current.immutable();
            BlockState state = level.getBlockState(pos);
            if (state.isFaceSturdy(level, pos, faceDirection))
                return pos;
            current.move(faceDirection.getOpposite());
        }

        return from;
    }

    public static BlockPos findCeiling(LevelAccessor level, BlockPos from) {
        return findFaceSturdy(level, from, Direction.DOWN);
    }

    public static BlockPos findFloor(LevelAccessor level, BlockPos from) {
        return findFaceSturdy(level, from, Direction.UP);
    }
}

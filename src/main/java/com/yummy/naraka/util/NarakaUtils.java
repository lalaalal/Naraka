package com.yummy.naraka.util;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.function.Consumer;

/**
 * NarakaUtils
 */
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

    public static void sphere(BoundingBox box, float size, Consumer<Vec3i> consumer) {
        sphere(box, size, (x, y, z) -> consumer.accept(new Vec3i(x, y, z)));
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
}
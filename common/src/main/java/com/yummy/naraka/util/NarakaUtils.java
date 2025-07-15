package com.yummy.naraka.util;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public class NarakaUtils {
    private static final float A2 = 0.41421f;
    private static final float A3 = 0.30277f;

    public static AnimationChannel.Interpolation easeInOut(float partition) {
        return (vector3f, delta, keyframes, start, end, scale) -> {
            Vector3f vectorStart = keyframes[start].target();
            Vector3f vectorEnd = keyframes[end].target();
            return easeInOut(delta, partition, vectorStart, vectorEnd, vector3f);
        };
    }

    public static float fastStepIn(float x) {
        final float a = A3;
        return ((-1 / (3 * x + a)) + a) / 3 + 1;
    }

    public static float fastStepOut(float x) {
        final float a = A3;
        return ((-1 / (3 * (x - 1) - a)) - a) / 3;
    }

    public static float easeInOut(float delta, float partition, float start, float end) {
        float middle = (start + end) * partition;
        if (delta < partition) {
            float newDelta = fastStepOut(delta * (1 / partition));
            return Mth.lerp(Math.clamp(newDelta, 0, 1), start, middle);
        } else {
            float newDelta = fastStepIn((delta - partition) * (1 / (1 - partition)));
            return Mth.lerp(Math.clamp(newDelta, 0, 1), middle, end);
        }
    }

    public static Vector3f easeInOut(float delta, float partition, Vector3f start, Vector3f end, Vector3f dest) {
        return dest.set(
                easeInOut(delta, partition, start.x, end.x),
                easeInOut(delta, partition, start.y, end.y),
                easeInOut(delta, partition, start.z, end.z)
        );
    }

    public static float interpolate(float delta, float start, float end, Function<Float, Float> function) {
        float newDelta = function.apply(delta);
        return Mth.lerp(Math.clamp(newDelta, 0, 1), start, end);
    }

    @FunctionalInterface
    public interface PositionConsumer {
        void accept(int x, int y, int z);
    }

    @FunctionalInterface
    public interface LengthPositionConsumer {
        void accept(double length, int x, int y, int z);
    }

    public static final BiPredicate<BlockPos, Integer> OUTLINE = (position, radius) -> {
        int xz = position.getX() * position.getX() + position.getZ() * position.getZ();
        return (radius - 0.75) * (radius - 0.75) <= xz && xz <= (radius + 0.25) * (radius + 0.25);
    };

    public static void circle(BlockPos center, int radius, BiPredicate<BlockPos, Integer> predicate, Consumer<BlockPos> consumer) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (predicate.test(new BlockPos(x, center.getY(), z), radius)) {
                    consumer.accept(center.offset(x, 0, z));
                }
            }
        }
    }

    public static double wrapRadians(double angle) {
        double result = angle % Math.TAU;
        if (result >= Math.PI) {
            result -= Math.TAU;
        }

        if (result < -Math.PI) {
            result += Math.TAU;
        }

        return result;
    }

    public static void sphere(BoundingBox box, float size, PositionConsumer consumer) {
        sphere(box, box, size, (length, x, y, z) -> consumer.accept(x, y, z));
    }

    public static void sphere(BoundingBox box, BoundingBox part, float size, LengthPositionConsumer consumer) {
        double xRadius = (float) (box.maxX() - box.minX() + 1) / 2;
        double zRadius = (float) (box.maxZ() - box.minZ() + 1) / 2;
        double yRadius = (float) (box.maxY() - box.minY() + 1) / 2;
        double centerX = box.minX() + xRadius;
        double centerZ = box.minZ() + zRadius;
        double centerY = box.minY() + yRadius;

        int xMin = Math.max(box.minX(), part.minX());
        int xMax = Math.min(box.maxX(), part.maxX());
        int yMin = Math.max(box.minY(), part.minY());
        int yMax = Math.min(box.maxY(), part.maxY());
        int zMin = Math.max(box.minZ(), part.minZ());
        int zMax = Math.min(box.maxZ(), part.maxZ());

        for (int y = yMin; y <= yMax; y++) {
            double yRatio = (y - centerY) / yRadius;
            for (int x = xMin; x <= xMax; x++) {
                double xRatio = (x - centerX) / xRadius;
                for (int z = zMin; z <= zMax; z++) {
                    double zRatio = (z - centerZ) / zRadius;
                    double length = xRatio * xRatio + yRatio * yRatio + zRatio * zRatio;
                    if (length <= size)
                        consumer.accept(length, x, y, z);
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
        double xRadius = (float) (box.maxX() - box.minX() + 1) / 2;
        double zRadius = (float) (box.maxZ() - box.minZ() + 1) / 2;
        double yRadius = (float) (box.maxY() - box.minY() + 1) / 2;
        double centerX = box.minX() + xRadius;
        double centerZ = box.minZ() + zRadius;
        double centerY = box.minY() + yRadius;
        double xRatio = (pos.getX() - centerX) / xRadius;
        double zRatio = (pos.getZ() - centerZ) / zRadius;
        double yRatio = (pos.getY() - centerY) / yRadius;

        return xRatio * xRatio + yRatio * yRatio + zRatio * zRatio <= size;
    }

    public static Vec3 vec3(Vec3i pos) {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockPos pos(Vec3 pos) {
        return new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);
    }

    public static BlockPos findCollision(LevelAccessor level, BlockPos from, Direction faceDirection) {
        BlockPos.MutableBlockPos current = from.mutable();
        while (level.isInsideBuildHeight(current.getY())) {
            BlockPos pos = current.immutable();
            BlockState state = level.getBlockState(pos);
            if (!state.getCollisionShape(level, pos).isEmpty())
                return pos;
            current.move(faceDirection.getOpposite());
        }

        return from;
    }

    public static BlockPos findCeiling(LevelAccessor level, BlockPos from) {
        return findCollision(level, from, Direction.DOWN);
    }

    public static BlockPos findFloor(LevelAccessor level, BlockPos from) {
        return findCollision(level, from, Direction.UP);
    }

    public static BlockPos findAir(LevelAccessor level, BlockPos from, Direction findingDirection) {
        if (level.isInsideBuildHeight(from.getY()))
            return from;
        BlockPos findingPos = from.relative(findingDirection);
        BlockState state = level.getBlockState(findingPos);
        if (state.isCollisionShapeFullBlock(level, findingPos))
            return findAir(level, findingPos, findingDirection);
        return findingPos;
    }

    public static BlockPos randomBlockPos(RandomSource random, BlockPos pos, int offset) {
        int x = pos.getX() + random.nextIntBetweenInclusive(-offset, offset);
        int z = pos.getZ() + random.nextIntBetweenInclusive(-offset, offset);
        int y = pos.getY();

        return new BlockPos(x, y, z);
    }

    public static <T, U> void iterate(List<T> list1, List<U> list2, BiConsumer<T, U> consumer) {
        if (list1.size() != list2.size())
            throw new IllegalArgumentException("Length of array1, array2 does not match");
        for (int index = 0; index < list1.size(); index++)
            consumer.accept(list1.get(index), list2.get(index));
    }

    public static <T, U> void iterate(List<T> list1, List<U> list2, BiConsumer<T, U> consumer, boolean reversed) {
        if (reversed)
            iterate(list1.reversed(), list2.reversed(), consumer);
        else
            iterate(list1, list2, consumer);
    }

    public static Vec3 projection(Vec3 target, Vec3 base) {
        base = base.normalize();
        return base.scale(target.dot(base) / base.length());
    }
}

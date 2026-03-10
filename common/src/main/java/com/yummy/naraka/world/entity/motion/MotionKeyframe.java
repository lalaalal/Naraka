package com.yummy.naraka.world.entity.motion;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

public record MotionKeyframe<T>(int tick, T value, Interpolation<T> interpolation) {
    public static Builder<Vec3> position(int tick) {
        return new Builder<>(tick, Vec3.ZERO, Interpolation.V_LINEAR);
    }

    public static Builder<Vec3> position(int tick, double x, double y, double z) {
        return new Builder<>(tick, new Vec3(x, y, z), Interpolation.V_LINEAR);
    }

    public static Builder<Quaternionfc> rotation(int tick) {
        return new Builder<>(tick, new Quaternionf(), Interpolation.Q_LINEAR);
    }

    public static class Builder<T> {
        private final int tick;
        private T value;
        private Interpolation<T> interpolation;

        public Builder(int tick, T value, Interpolation<T> interpolation) {
            this.tick = tick;
            this.value = value;
            this.interpolation = interpolation;
        }

        public Builder<T> value(T value) {
            this.value = value;
            return this;
        }

        public Builder<T> interpolation(Interpolation<T> interpolation) {
            this.interpolation = interpolation;
            return this;
        }

        public MotionKeyframe<T> build() {
            return new MotionKeyframe<>(tick, value, interpolation);
        }
    }
}

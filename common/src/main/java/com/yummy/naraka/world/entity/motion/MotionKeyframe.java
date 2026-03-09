package com.yummy.naraka.world.entity.motion;

import net.minecraft.world.phys.Vec3;

public record MotionKeyframe(int tick, Vec3 value, Interpolation interpolation) implements Interpolation {
    public static final MotionKeyframe DEFAULT = new MotionKeyframe(0, Vec3.ZERO, Interpolation.LINEAR);

    public static Builder builder(SwordMotionChannel.Builder parent, int tick) {
        return new Builder(parent, tick);
    }

    @Override
    public Vec3 interpolate(float delta, Vec3 from, Vec3 to) {
        return interpolation.interpolate(delta, from, to);
    }

    public static class Builder {
        private final SwordMotionChannel.Builder parent;
        private final int tick;
        private Vec3 value = Vec3.ZERO;
        private Interpolation interpolation = DEFAULT;

        public Builder(SwordMotionChannel.Builder parent, int tick) {
            this.parent = parent;
            this.tick = tick;
        }

        public Builder value(Vec3 value) {
            this.value = value;
            return this;
        }

        public Builder value(double x, double y, double z) {
            return value(new Vec3(x, y, z));
        }

        public Builder interpolation(Interpolation interpolation) {
            this.interpolation = interpolation;
            return this;
        }

        public SwordMotionChannel.Builder build() {
            return parent.add(new MotionKeyframe(tick, value, interpolation));
        }
    }
}

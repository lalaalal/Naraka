package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.world.entity.NarakaSword;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SwordMotionChannel {
    private int tickCount = 0;
    private final List<MotionKeyframe> keyframes;
    private int currentKeyframeIndex = -1;
    private Vec3 original = Vec3.ZERO;
    private final MotionApplier motionApplier;

    public static Builder builder(MotionApplier motionApplier) {
        return new Builder(motionApplier);
    }

    public static Builder translation() {
        return new Builder(MotionApplier.POSITION);
    }

    public static Builder rotation() {
        return new Builder(MotionApplier.ROTATION);
    }

    private SwordMotionChannel(List<MotionKeyframe> keyframes, MotionApplier motionApplier) {
        this.keyframes = List.copyOf(keyframes);
        this.motionApplier = motionApplier;
    }

    public void tick(NarakaSword entity) {
        if (tickCount == 0)
            original = motionApplier.get(entity);
        if (isCurrentKeyframeFinished())
            currentKeyframeIndex += 1;

        MotionKeyframe currentKeyframe = keyframes.get(currentKeyframeIndex);
        MotionKeyframe nextKeyframe = getNextKeyframe();
        apply(entity, currentKeyframe, nextKeyframe);

        tickCount += 1;
    }

    private boolean isCurrentKeyframeFinished() {
        return currentKeyframeIndex + 1 < keyframes.size() && keyframes.get(currentKeyframeIndex + 1).tick() <= tickCount;
    }

    private MotionKeyframe getNextKeyframe() {
        if (currentKeyframeIndex + 1 < keyframes.size())
            return keyframes.get(currentKeyframeIndex + 1);
        return keyframes.get(currentKeyframeIndex);
    }

    private void apply(NarakaSword entity, MotionKeyframe current, MotionKeyframe next) {
        float length = next.tick() - current.tick();
        float delta = (tickCount - current.tick()) / length;
        if (length <= 0)
            delta = 1;

        Vec3 value = next.interpolate(delta, current.value(), next.value());
        motionApplier.apply(entity, original.add(value));
    }

    public static class Builder {
        private final MotionApplier motionApplier;
        private final List<MotionKeyframe> keyframes = new ArrayList<>();

        public Builder(MotionApplier motionApplier) {
            this.motionApplier = motionApplier;
        }

        public Builder add(MotionKeyframe keyframe) {
            keyframes.add(keyframe);
            return this;
        }

        public MotionKeyframe.Builder keyframe(int tick) {
            return MotionKeyframe.builder(this, tick);
        }

        public MotionApplier getMotionApplier() {
            return motionApplier;
        }

        public SwordMotionChannel build() {
            if (keyframes.isEmpty())
                throw new IllegalStateException("Creating empty motion");

            return new SwordMotionChannel(keyframes, motionApplier);
        }
    }
}

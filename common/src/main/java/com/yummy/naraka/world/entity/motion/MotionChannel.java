package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.world.entity.Motionable;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;

import java.util.ArrayList;
import java.util.List;

public class MotionChannel<T> {
    private int tickCount = 0;
    private final List<MotionKeyframe<T>> keyframes;
    private int currentKeyframeIndex = -1;

    private T original;
    private final MotionApplier<T> motionApplier;

    public static Builder<Vec3> translation(boolean relative) {
        return new Builder<>(MotionApplier.position(relative));
    }

    public static Builder<Quaternionfc> rotation(boolean relative) {
        return new Builder<>(MotionApplier.rotation(relative));
    }

    private MotionChannel(List<MotionKeyframe<T>> keyframes, MotionApplier<T> motionApplier) {
        this.keyframes = List.copyOf(keyframes);
        this.motionApplier = motionApplier;
    }

    public void tick(Motionable motionable) {
        if (tickCount == 0)
            original = motionApplier.get(motionable);
        if (isCurrentKeyframeFinished())
            currentKeyframeIndex += 1;

        MotionKeyframe<T> currentKeyframe = keyframes.get(currentKeyframeIndex);
        MotionKeyframe<T> nextKeyframe = getNextKeyframe();
        apply(motionable, currentKeyframe, nextKeyframe);

        tickCount += 1;
    }

    private boolean isCurrentKeyframeFinished() {
        return currentKeyframeIndex + 1 < keyframes.size() && keyframes.get(currentKeyframeIndex + 1).tick() <= tickCount;
    }

    private MotionKeyframe<T> getNextKeyframe() {
        if (currentKeyframeIndex + 1 < keyframes.size())
            return keyframes.get(currentKeyframeIndex + 1);
        return keyframes.get(currentKeyframeIndex);
    }

    private void apply(Motionable motionable, MotionKeyframe<T> current, MotionKeyframe<T> next) {
        float length = next.tick() - current.tick();
        float delta = (tickCount - current.tick()) / length;
        if (length <= 0)
            delta = 1;

        T value = next.interpolation().interpolate(delta, current.value(), next.value());
        motionApplier.apply(motionable, original, value);
    }

    public static class Builder<T> {
        private final MotionApplier<T> motionApplier;
        private final List<MotionKeyframe<T>> keyframes = new ArrayList<>();

        public Builder(MotionApplier<T> motionApplier) {
            this.motionApplier = motionApplier;
        }

        public Builder<T> keyframe(MotionKeyframe<T> keyframe) {
            keyframes.add(keyframe);
            return this;
        }

        public Builder<T> keyframe(MotionKeyframe.Builder<T> builder) {
            return keyframe(builder.build());
        }

        public MotionChannel<T> build() {
            if (keyframes.isEmpty())
                throw new IllegalStateException("Creating empty motion");

            return new MotionChannel<>(keyframes, motionApplier);
        }
    }
}

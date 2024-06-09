package com.yummy.naraka.client.animation;

import com.yummy.naraka.NarakaUtil;
import net.minecraft.client.model.geom.PartPose;

/**
 * @param partName
 * @param tick
 * @param pose
 * @param animationTimingFunction
 * @author lalaalal
 */
public record Keyframe(String partName, int tick, PartPose pose, AnimationTimingFunction animationTimingFunction) {
    public PartPose transform(float delta, PartPose from, PartPose to) {
        return animationTimingFunction.transform(delta, from, to);
    }

    public static class Builder {
        private final Animation.Builder animationBuilder;
        private final String partName;
        private final int tick;
        private PartPose pose = PartPose.ZERO;
        private AnimationTimingFunction animationTimingFunction = AnimationTimingFunction.LINEAR;

        public Builder(Animation.Builder animationBuilder, String partName, int tick) {
            this.animationBuilder = animationBuilder;
            this.partName = partName;
            this.tick = tick;
        }

        public String getPartName() {
            return partName;
        }

        public int getTick() {
            return tick;
        }

        public Builder zero() {
            this.pose = PartPose.ZERO;
            return this;
        }

        public Builder copyPrevious() {
            Builder previousBuilder = animationBuilder.previousKeyframe(this);
            this.pose = previousBuilder.pose;
            this.animationTimingFunction = previousBuilder.animationTimingFunction;
            return this;
        }

        public Builder rotation(float xRot, float yRot, float zRot) {
            this.pose = PartPose.rotation(xRot, yRot, zRot);
            return this;
        }

        public Builder rotation(int xRot, int yRot, int zRot) {
            return rotation(NarakaUtil.radian(xRot), NarakaUtil.radian(yRot), NarakaUtil.radian(zRot));
        }

        public Builder offset(float x, float y, float z) {
            this.pose = PartPose.offset(x, y, z);
            return this;
        }

        public Builder offsetAndRotation(float x, float y, float z, float xRot, float yRot, float zRot) {
            this.pose = PartPose.offsetAndRotation(x, y, z, xRot, yRot, zRot);
            return this;
        }

        public Builder animationTiming(AnimationTimingFunction animationTimingFunction) {
            this.animationTimingFunction = animationTimingFunction;
            return this;
        }

        public Builder easeIn() {
            this.animationTimingFunction = AnimationTimingFunction.EASE_IN;
            return this;
        }

        public Builder easeOut() {
            this.animationTimingFunction = AnimationTimingFunction.EASE_OUT;
            return this;
        }

        public Builder easeInOut() {
            this.animationTimingFunction = AnimationTimingFunction.EASE_IN_OUT;
            return this;
        }

        public Animation.Builder end() {
            return animationBuilder;
        }

        public Keyframe build() {
            return new Keyframe(partName, tick, pose, animationTimingFunction);
        }
    }
}

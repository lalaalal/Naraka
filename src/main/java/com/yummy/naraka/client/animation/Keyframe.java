package com.yummy.naraka.client.animation;

import com.yummy.naraka.NarakaUtil;
import net.minecraft.client.model.geom.PartPose;

/**
 * Keyframe applying each part model
 *
 * @author lalaalal
 */
public record Keyframe(String partName, int tick, PartPose pose, AnimationTiming animationTiming) {
    public PartPose applyAnimation(float delta, PartPose from) {
        return animationTiming.apply(delta, from, pose);
    }

    public static class Builder {
        private final Animation.Builder animationBuilder;
        private final String partName;
        private final int tick;
        private PartPose pose = PartPose.ZERO;
        private AnimationTiming animationTiming = AnimationTiming.LINEAR;

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
            this.animationTiming = previousBuilder.animationTiming;
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

        public Builder animationTiming(AnimationTiming animationTiming) {
            this.animationTiming = animationTiming;
            return this;
        }

        public Builder easeIn() {
            return animationTiming(AnimationTiming.EASE_IN);
        }

        public Builder easeOut() {
            return animationTiming(AnimationTiming.EASE_OUT);
        }

        public Builder easeInOut() {
            return animationTiming(AnimationTiming.EASE_IN_OUT);
        }

        public Animation.Builder end() {
            return animationBuilder;
        }

        public Keyframe build() {
            return new Keyframe(partName, tick, pose, animationTiming);
        }
    }
}

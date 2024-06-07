package com.yummy.naraka.client.animation;

/**
 * @param partName
 * @param tick
 * @param pose
 * @param transformMethod
 * @author lalaalal
 */
public record Keyframe(String partName, int tick, ModelPartPose pose, TransformMethod transformMethod) {
    public ModelPartPose transform(float delta, ModelPartPose from, ModelPartPose to) {
        return transformMethod.transform(delta, from, to);
    }

    public static class Builder {
        private final Animation.Builder animationBuilder;
        private final String partName;
        private final int tick;
        private ModelPartPose pose = ModelPartPose.ZERO;
        private TransformMethod transformMethod = TransformMethod.LINEAR;

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
            this.pose = ModelPartPose.ZERO;
            return this;
        }

        public Builder copyPrevious() {
            Builder previousBuilder = animationBuilder.previousKeyframe(this);
            this.pose = previousBuilder.pose;
            this.transformMethod = previousBuilder.transformMethod;
            return this;
        }

        public Builder pose(float xRot, float yRot, float zRot) {
            this.pose = new ModelPartPose(xRot, yRot, zRot);
            return this;
        }

        public Builder transformMethod(TransformMethod transformMethod) {
            this.transformMethod = transformMethod;
            return this;
        }

        public Builder easeIn() {
            this.transformMethod = TransformMethod.EASE_IN;
            return this;
        }

        public Builder easeOut() {
            this.transformMethod = TransformMethod.EASE_OUT;
            return this;
        }

        public Builder easeInOut() {
            this.transformMethod = TransformMethod.EASE_IN_OUT;
            return this;
        }

        public Animation.Builder end() {
            return animationBuilder;
        }

        public Keyframe build() {
            return new Keyframe(partName, tick, pose, transformMethod);
        }
    }
}

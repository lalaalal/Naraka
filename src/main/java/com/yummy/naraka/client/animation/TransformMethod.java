package com.yummy.naraka.client.animation;

/**
 * @author lalaalal
 */
public interface TransformMethod {
    TransformMethod EASE_IN_OUT = ModelPartPose::easeInOut;
    TransformMethod EASE_IN = ModelPartPose::easeIn;
    TransformMethod EASE_OUT = ModelPartPose::easeOut;
    TransformMethod LINEAR = ModelPartPose::lerp;

    ModelPartPose transform(float delta, ModelPartPose from, ModelPartPose to);
}

package com.yummy.naraka.client.animation;

import net.minecraft.client.model.geom.ModelPart;

/**
 * @author lalaalal
 */
public class PartAnimationInstance {
    private final Animation animation;
    private final PartAnimation partAnimation;
    private final boolean repeat;
    private Keyframe currentKeyframe;
    private ModelPartPose originalPose;
    private float firstTick = -1;

    public PartAnimationInstance(Animation animation, PartAnimation partAnimation, boolean repeat) {
        this.animation = animation;
        this.partAnimation = partAnimation;
        this.currentKeyframe = partAnimation.getFirstKeyframe();
        this.repeat = repeat;
    }

    public void setupPartAnimation(ModelPart part, float ageInTicks) {
        if (firstTick < 0)
            ready(part, ageInTicks);
        if (isAnimationFinished(ageInTicks) && repeat)
            ready(part, ageInTicks);
        if (!partAnimation.hasNextKeyframe(currentKeyframe))
            return;
        Keyframe nextKeyframe = partAnimation.getNextKeyframe(currentKeyframe);
        float delta = calculateDelta(ageInTicks, nextKeyframe);
        if (0 <= delta && delta <= 1)
            applyAnimation(part, nextKeyframe, delta);
        else {
            currentKeyframe = nextKeyframe;
            setupPartAnimation(part, ageInTicks);
        }
    }

    private void ready(ModelPart part, float ageInTicks) {
        firstTick = ageInTicks;
        originalPose = ModelPartPose.from(part);
        currentKeyframe = partAnimation.getFirstKeyframe();
    }

    private boolean isAnimationFinished(float ageInTicks) {
        float relativeTick = ageInTicks - firstTick;
        return relativeTick >= animation.animationLength();
    }

    private void applyAnimation(ModelPart part, Keyframe nextKeyframe, float delta) {
        ModelPartPose from = getPoseFrom();
        ModelPartPose to = getPoseTo(nextKeyframe);

        ModelPartPose currentPose = nextKeyframe.transform(delta, from, to);
        currentPose.applyTo(part);
    }

    private float calculateDelta(float ageInTicks, Keyframe nextKeyframe) {
        float interval = calculateTickInterval(nextKeyframe);
        if (interval == 0)
            return -1;
        float relativeTick = ageInTicks - firstTick - currentKeyframe.tick();

        return relativeTick / interval;
    }

    private float calculateTickInterval(Keyframe nextKeyframe) {
        if (partAnimation.isFirstKeyframe(currentKeyframe))
            return nextKeyframe.tick();
        return nextKeyframe.tick() - currentKeyframe.tick();
    }

    private ModelPartPose getPoseFrom() {
        if (partAnimation.isFirstKeyframe(currentKeyframe) && currentKeyframe.tick() != 0)
            return originalPose;
        return currentKeyframe.pose();
    }

    private ModelPartPose getPoseTo(Keyframe nextKeyframe) {
        if (partAnimation.isFirstKeyframe(currentKeyframe) && currentKeyframe.tick() != 0)
            return currentKeyframe.pose();
        return nextKeyframe.pose();
    }
}

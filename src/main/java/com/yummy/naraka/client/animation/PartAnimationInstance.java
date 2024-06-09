package com.yummy.naraka.client.animation;

import com.yummy.naraka.NarakaUtil;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;

/**
 * @author lalaalal
 */
public class PartAnimationInstance {
    private final Animation animation;
    private final PartAnimation partAnimation;
    private final boolean repeat;
    private Keyframe currentKeyframe;
    private PartPose originalPose;
    private float firstTick = -1;
    private float prevTick = -1;

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
        if (0 <= delta && delta <= 1) {
            applyAnimation(part, nextKeyframe, delta);
            prevTick = ageInTicks;
        } else {
            if (!isCurrentTickEmptyStart())
                currentKeyframe = nextKeyframe;
            prevTick = ageInTicks;
            setupPartAnimation(part, ageInTicks);
        }
    }

    private void ready(ModelPart part, float ageInTicks) {
        firstTick = ageInTicks;
        originalPose = NarakaUtil.rotationOnly(part.storePose());
        currentKeyframe = partAnimation.getFirstKeyframe();
    }

    private boolean isAnimationFinished(float ageInTicks) {
        float relativeTick = ageInTicks - firstTick;
        return relativeTick >= animation.animationLength();
    }

    private void applyAnimation(ModelPart part, Keyframe nextKeyframe, float delta) {
        PartPose from = getPoseFrom();
        PartPose to = getPoseTo(nextKeyframe);
        PartPose currentPose = nextKeyframe.transform(delta, from, to);
        part.loadPose(NarakaUtil.addOffsetOnly(currentPose, part.getInitialPose()));
    }

    private float calculateDelta(float ageInTicks, Keyframe nextKeyframe) {
        float interval = calculateTickInterval(nextKeyframe);
        if (interval == 0)
            return -1;
        float relativeTick = ageInTicks - firstTick;
        if (!isCurrentTickEmptyStart())
            relativeTick -= currentKeyframe.tick();

        return relativeTick / interval;
    }

    private float calculateTickInterval(Keyframe nextKeyframe) {
        if (isCurrentTickEmptyStart())
            return currentKeyframe.tick();
        return nextKeyframe.tick() - currentKeyframe.tick();
    }

    private PartPose getPoseFrom() {
        if (isCurrentTickEmptyStart())
            return originalPose;
        return currentKeyframe.pose();
    }

    private PartPose getPoseTo(Keyframe nextKeyframe) {
        if (isCurrentTickEmptyStart())
            return currentKeyframe.pose();
        return nextKeyframe.pose();
    }

    private boolean isCurrentTickEmptyStart() {
        return partAnimation.isEmptyStart(currentKeyframe) && (prevTick - firstTick) < currentKeyframe.tick();
    }
}

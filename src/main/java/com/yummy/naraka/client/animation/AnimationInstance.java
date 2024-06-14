package com.yummy.naraka.client.animation;

import net.minecraft.client.model.geom.ModelPart;

import java.util.HashMap;
import java.util.Map;

/**
 * Instance of {@link Animation}
 *
 * @author lalaalal
 */
public class AnimationInstance {
    private final Animation animation;
    private final boolean repeat;
    private final Map<String, PartAnimationInstance> partAnimationInstances = new HashMap<>();
    private boolean finished = false;
    private float firstTick = -1;
    private AnimationInstance chain;

    public AnimationInstance(Animation animation, boolean repeat) {
        this.animation = animation;
        this.repeat = repeat;

        for (PartAnimation partAnimation : animation.partAnimations().values())
            partAnimationInstances.put(partAnimation.getName(), partAnimation.createInstance(animation, repeat));
    }

    public AnimationInstance(Animation animation, AnimationInstance chain) {
        this(animation, false);
        this.chain = chain;
    }

    public String getId() {
        String id = animation.name();
        if (chain != null)
            id += ".chain." + chain.getId();
        if (isRepeat())
            id += ".repeat";
        return id;
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isRepeat() {
        return chain != null && repeat;
    }

    public boolean isFinished() {
        if (chain != null)
            return chain.isFinished();
        return !repeat && finished;
    }

    public void setupAnimation(Map<String, ModelPart> partMap, float ageInTicks) {
        if (firstTick < 0)
            firstTick = ageInTicks;
        if (!repeat && isAnimationFinished(ageInTicks)) {
            if (!finished)
                setupPartAnimations(partMap, firstTick + animation.animationLength());
            if (chain != null)
                chain.setupAnimation(partMap, ageInTicks);
            finished = true;
            return;
        }
        setupPartAnimations(partMap, ageInTicks);
    }

    private void setupPartAnimations(Map<String, ModelPart> partMap, float ageInTicks) {
        for (String partName : animation.partNames()) {
            PartAnimationInstance partAnimationInstance = partAnimationInstances.get(partName);
            ModelPart part = partMap.get(partName);
            partAnimationInstance.setupPartAnimation(part, ageInTicks);
        }
    }

    private boolean isAnimationFinished(float ageInTicks) {
        float relativeTick = ageInTicks - firstTick;
        return relativeTick > animation.animationLength();
    }
}

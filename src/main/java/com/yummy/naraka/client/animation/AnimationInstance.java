package com.yummy.naraka.client.animation;

import net.minecraft.client.model.geom.ModelPart;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lalaalal
 */
public class AnimationInstance {
    private final Animation animation;
    private final boolean repeat;
    private final Map<String, PartAnimationInstance> partAnimationInstances = new HashMap<>();

    public AnimationInstance(Animation animation, boolean repeat) {
        this.animation = animation;
        this.repeat = repeat;

        for (PartAnimation partAnimation : animation.partAnimations().values())
            partAnimationInstances.put(partAnimation.getName(), partAnimation.createInstance(animation, repeat));
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setupAnimation(Map<String, ModelPart> partMap, float ageInTicks) {
        for (String partName : partMap.keySet()) {
            PartAnimationInstance partAnimationInstance = partAnimationInstances.get(partName);
            ModelPart part = partMap.get(partName);
            partAnimationInstance.setupPartAnimation(part, ageInTicks);
        }
    }
}

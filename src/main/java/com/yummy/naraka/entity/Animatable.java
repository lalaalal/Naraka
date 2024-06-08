package com.yummy.naraka.entity;

import com.yummy.naraka.client.animation.AnimationInstance;

/**
 * @author lalaalal
 */
public interface Animatable {
    boolean isAnimationRepeating();

    boolean isAnimationFinished();

    AnimationInstance getAnimationInstance();

    void setAnimation(String animationName);
}

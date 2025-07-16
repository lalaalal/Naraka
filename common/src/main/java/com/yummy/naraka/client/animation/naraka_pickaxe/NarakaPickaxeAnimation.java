package com.yummy.naraka.client.animation.naraka_pickaxe;

import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.client.animation.NarakaInterpolations;
import com.yummy.naraka.world.entity.animation.NarakaPickaxeAnimationLocations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

@Environment(EnvType.CLIENT)
public class NarakaPickaxeAnimation {
    public static void initialize() {
        AnimationMapper.register(NarakaPickaxeAnimationLocations.SWING, SWING);
        AnimationMapper.register(NarakaPickaxeAnimationLocations.EXPLODE, EXPLODE);
        AnimationMapper.register(NarakaPickaxeAnimationLocations.IDLE, IDLE);
    }

    public static final AnimationDefinition SWING = AnimationDefinition.Builder.withLength(7.0F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -360.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -80.0F, -500.0F), NarakaInterpolations.FAST_STEP_IN),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, -80.0F, -500.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-67.3529F, -63.194F, -430.4248F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.375F, KeyframeAnimations.degreeVec(-67.35F, -460.0F, -430.42F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.0F, KeyframeAnimations.degreeVec(-67.35F, -860.0F, -430.42F), NarakaInterpolations.FAST_STEP_IN),
                    new Keyframe(7.0F, KeyframeAnimations.degreeVec(0.0F, -720.0F, -360.0F), NarakaInterpolations.FAST_STEP_OUT)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition EXPLODE = AnimationDefinition.Builder.withLength(2.0F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -900.0F), NarakaInterpolations.FAST_STEP_IN)
            ))
            .build();

    public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(0)
            .build();
}

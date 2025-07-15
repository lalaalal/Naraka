package com.yummy.naraka.client.animation.herobrine;

import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.client.animation.NarakaInterpolations;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Made with Blockbench 4.12.2
 *
 * @author noon414, joachimbeen
 */
@Environment(EnvType.CLIENT)
public class HerobrineComboAttackAnimation {
    public static void initialize() {
        AnimationMapper.register(AnimationLocations.COMBO_ATTACK_1, COMBO_ATTACK_1);
        AnimationMapper.register(AnimationLocations.COMBO_ATTACK_2, COMBO_ATTACK_2);
        AnimationMapper.register(AnimationLocations.COMBO_ATTACK_3, COMBO_ATTACK_3);
        AnimationMapper.register(AnimationLocations.COMBO_ATTACK_4, COMBO_ATTACK_4);
        AnimationMapper.register(AnimationLocations.COMBO_ATTACK_5, COMBO_ATTACK_5);
    }

    public static final AnimationDefinition COMBO_ATTACK_1 = AnimationDefinition.Builder.withLength(1.125F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 70.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -47.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 40.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 80.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-82.5F, 0.0F, 80.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-82.4929F, -2.4786F, 79.6735F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-36.9224F, 0.0715F, 10.1125F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(-1.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(-2.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(-2.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, -0.5F, -0.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-63.33F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-31.67F, 0.0F, -26.67F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(10.0F, 0.0F, -80.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(43.4679F, -19.1294F, -2.4316F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(43.4679F, -19.1294F, -2.4316F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(2.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, -7.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-30.411F, -5.7162F, 0.7189F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-30.411F, -5.7162F, 0.7189F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition COMBO_ATTACK_2 = AnimationDefinition.Builder.withLength(1.75F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(37.0701F, -35.227F, -17.8211F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(39.5701F, -35.227F, -17.8211F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-8.2517F, 14.7458F, -16.1804F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-8.2517F, 14.7458F, -16.1804F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(31.8177F, -27.0961F, -11.9636F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.3333F, KeyframeAnimations.degreeVec(34.32F, -27.1F, -11.96F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(34.32F, -27.1F, -11.96F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-10.3195F, 27.193F, -0.0572F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-10.3195F, 27.193F, -0.0572F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-19.1568F, -4.972F, 5.0597F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-19.1568F, -4.972F, 5.0597F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-19.1025F, -2.61F, 4.2387F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.9041F, 7.3888F, 4.3958F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.9041F, 7.3888F, 4.3958F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-82.4929F, -2.4786F, 79.6735F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-82.1405F, -17.3455F, 77.6433F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-82.1405F, -17.3455F, 77.6433F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-6.1656F, -2.1259F, 19.179F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(31.4656F, 10.524F, 34.6716F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(31.4656F, 10.524F, 34.6716F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-49.4049F, -9.8406F, 52.6049F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-49.4049F, -9.8406F, 52.6049F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-2.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(-2.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(-2.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -0.33F, -0.33F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.posVec(-1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(-1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-42.43F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-42.43F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-44.43F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-49.93F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-49.93F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(43.4679F, -19.1294F, -2.4316F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(45.9679F, -19.1294F, -2.4316F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-133.39F, -25.09F, 2.49F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-133.39F, -25.09F, 2.49F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.degreeVec(29.3672F, -18.8418F, -18.0268F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(29.3672F, -18.8418F, -18.0268F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-85.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-85.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1.0417F, KeyframeAnimations.posVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.degreeVec(12.7936F, -12.1991F, 19.7529F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(12.7936F, -12.1991F, 19.7529F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 1.0F, -2.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 1.0F, -2.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-30.411F, -5.7162F, 0.7189F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-42.91F, -5.72F, 0.72F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-42.91F, -5.72F, 0.72F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(2.4976F, -0.109F, 2.4976F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(2.4976F, -0.109F, 2.4976F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.6621F, -20.0901F, 1.5898F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-39.84F, -20.09F, 1.59F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-39.84F, -20.09F, 1.59F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1667F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition COMBO_ATTACK_3 = AnimationDefinition.Builder.withLength(1.0F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 5.0F), NarakaInterpolations.FAST_STEP_IN)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-8.2517F, 14.7458F, -16.1804F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.1423F, -40.0023F, 9.9061F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-27.0491F, -36.0059F, 21.5538F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.1423F, -40.0023F, 9.9061F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-19.1568F, -4.972F, 5.0597F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(6.5449F, 21.1742F, 0.9676F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(6.5449F, 21.1742F, 0.9676F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(31.4656F, 10.524F, 34.6716F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(66.5944F, 3.5203F, 59.9154F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(66.5944F, 3.5203F, 59.9154F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(-1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-42.43F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-57.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-57.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-133.39F, -25.09F, 2.49F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-72.6605F, 2.9372F, -70.8169F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-72.6605F, 2.9372F, -70.8169F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -30.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -40.75F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-26.5013F, 25.7048F, 64.8675F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-26.5013F, 25.7048F, 64.8675F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-47.7041F, 10.5042F, 49.495F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.4976F, -0.109F, 2.4976F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.6595F, -20.0892F, 1.5883F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(2.6595F, -20.0892F, 1.5883F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition COMBO_ATTACK_4 = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-16.41F, -18.67F, -6.72F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-21.41F, -18.67F, -6.72F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-13.3902F, 14.1033F, -19.1875F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.1423F, -40.0023F, 9.9061F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.1423F, -40.0023F, 9.9061F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.14F, -40.0F, 9.91F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.1423F, -40.0023F, 9.9061F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(6.5449F, 21.1742F, 0.9676F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(43.77F, 15.94F, 6.59F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(54.7934F, 10.7012F, 12.2122F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(35.5079F, 27.536F, 10.1116F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(66.5944F, 3.5203F, 59.9154F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-88.614F, 1.9765F, 31.3246F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-87.668F, 0.6235F, 71.3134F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(37.6447F, 21.4314F, 49.6253F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-1.0F, -2.0F, -2.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(-1.0F, -1.0F, -2.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-1.0F, -1.0F, -0.5F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-57.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-72.6605F, 2.9372F, -70.8169F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-70.5048F, -0.2389F, -33.498F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-98.3973F, 38.2282F, -35.3951F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.4795F, -1.8298F, -31.9819F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(1.0F, -2.0F, -2.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(-1.0F, -2.0F, -4.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(1.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -40.75F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, -40.75F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, -40.75F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -40.75F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-47.7041F, 10.5042F, 49.495F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-54.1251F, -15.5343F, 25.8775F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-54.1251F, -15.5343F, 25.8775F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 12.5F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.6595F, -20.0892F, 1.5883F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(20.676F, -31.9997F, 15.0005F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-0.33F, -16.67F, 3.79F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-7.3325F, -11.563F, 0.0482F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();


    public static final AnimationDefinition COMBO_ATTACK_5 = AnimationDefinition.Builder.withLength(2.75F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-13.3902F, 14.1033F, -19.1875F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-13.3902F, 14.1033F, -19.1875F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-13.3902F, 14.1033F, -19.1875F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-13.3902F, 14.1033F, -19.1875F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.1423F, -40.0023F, 9.9061F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-5.9071F, -33.2919F, 5.8808F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(65.1841F, -22.6206F, 43.1503F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(65.1841F, -22.6206F, 43.1503F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.5079F, 27.536F, 10.1116F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(18.4989F, 0.1186F, -8.0726F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(0.0F, -15.0F, -20.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -15.0F, -20.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.125F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, -10.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.6447F, 21.4314F, 49.6253F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(40.0F, -25.0F, 27.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(51.0071F, -17.3106F, 29.0414F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(51.0071F, -17.3106F, 29.0414F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -1.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.4795F, -1.8298F, -31.9819F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.4795F, -1.8298F, -31.9819F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-41.9198F, 24.5567F, -67.9682F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-63.0037F, 23.0574F, -92.7272F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-63.0037F, 23.0574F, -92.7272F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.125F, KeyframeAnimations.degreeVec(-39.3247F, -7.9972F, -9.6385F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(2.0F, 1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(2.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(2.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -40.75F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -40.75F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 12.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-4.6405F, 17.3455F, 14.8567F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-47.8783F, -7.3092F, 17.1689F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-47.8783F, -7.3092F, 17.1689F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(57.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(57.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.3325F, -11.563F, 0.0482F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(7.5F, 2.5F, 7.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(43.018F, -19.8885F, 24.8387F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(43.018F, -19.8885F, 24.8387F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg_lower", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}
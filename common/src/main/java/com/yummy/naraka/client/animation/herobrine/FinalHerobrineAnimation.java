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

@Environment(EnvType.CLIENT)
public class FinalHerobrineAnimation {
    public static void initialize() {
        AnimationMapper.register(AnimationLocations.ENTER_PHASE_3, ENTER_PHASE_3);
        AnimationMapper.register(AnimationLocations.PHASE_3_IDLE, IDLE);
        AnimationMapper.register(AnimationLocations.DYING, DYING);
        AnimationMapper.register(AnimationLocations.CHZZK, CHZZK);
        AnimationMapper.register(AnimationLocations.HIDDEN_CHZZK, HIDDEN_CHZZK);
    }

    public static final AnimationDefinition ENTER_PHASE_3 = AnimationDefinition.Builder.withLength(5.5F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5417F, KeyframeAnimations.degreeVec(-4.38F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-4.38F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-0.51F, -37.5F, -1.03F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(1.0348F, -52.4955F, -1.441F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(0.28F, -14.0F, -0.38F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), NarakaInterpolations.FAST_STEP_IN),
                    new Keyframe(4.0417F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), NarakaInterpolations.FAST_STEP_IN)
            ))
            .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-4.5942F, 25.3662F, -1.6678F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-7.8861F, 29.9685F, -1.4426F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-13.82F, -18.93F, 7.86F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(-16.1949F, -38.4905F, 11.577F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(-11.65F, -2.93F, 3.09F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(6.32F, -12.49F, -0.56F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(12.5881F, -14.9854F, -0.6696F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(39.02F, 36.08F, 16.77F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(49.5861F, 56.5012F, 23.7414F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(26.31F, 8.07F, 3.37F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 1.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5417F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.posVec(0.0F, 1.71F, -0.71F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.posVec(0.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.posVec(0.0F, 0.53F, -0.27F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-20.32F, 12.73F, 17.37F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-14.77F, 31.13F, 41.46F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-12.3894F, 34.784F, 45.8262F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-70.54F, -1.72F, 54.98F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.375F, KeyframeAnimations.degreeVec(-92.7F, -15.63F, 58.46F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(-93.8064F, -16.3222F, 58.6355F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(-11.67F, 10.73F, 29.48F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(18.1921F, 20.5607F, 18.8746F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(2.0F, KeyframeAnimations.posVec(-0.86F, -0.86F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5417F, KeyframeAnimations.posVec(-1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.posVec(-1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.posVec(-3.86F, -1.71F, -0.71F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.posVec(-5.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.posVec(-1.33F, -0.53F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-21.41F, 19.91F, -3.75F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5417F, KeyframeAnimations.degreeVec(-23.5097F, 24.5948F, -4.6293F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-23.5097F, 24.5948F, -4.6293F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-27.35F, 11.37F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(-28.8931F, 6.0803F, 3.2568F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(-9.54F, 1.62F, 0.87F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.81F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.29F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-20.0545F, -15.1339F, -26.6548F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5417F, KeyframeAnimations.degreeVec(-19.6197F, -18.8764F, -36.1426F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-11.2756F, -12.3734F, -78.1741F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(60.4678F, 18.4587F, 45.2277F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(-5.2436F, -29.5161F, 94.9459F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(-18.7055F, -25.6755F, 112.8261F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.375F, KeyframeAnimations.degreeVec(-34.9543F, -59.702F, 135.593F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(17.5F, 14.75F, -26.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(18.5F, 2.75F, -10.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5417F, KeyframeAnimations.posVec(19.3F, -1.28F, 9.22F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.posVec(0.8F, -0.6F, 2.13F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(1.9583F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.999F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.2073F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.2083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-15.95F, -14.51F, -23.66F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-40.27F, -5.59F, -30.01F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-43.8918F, -3.6525F, -30.7875F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(3.04F, -7.06F, -41.16F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(21.8188F, -8.418F, -45.3098F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(27.98F, -7.0F, -29.51F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(30.2136F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.86F, -0.86F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5417F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.posVec(0.27F, -0.27F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-28.22F, -23.06F, 7.97F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-40.84F, -28.48F, 9.85F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-42.5053F, -28.4804F, 9.8491F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-35.36F, -28.48F, 9.85F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(-32.5053F, -28.4804F, 9.8491F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(-25.17F, -7.59F, 2.63F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.81F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.27F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-16.92F, 8.87F, 13.47F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(-31.6813F, 12.4236F, 18.8591F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(-0.82F, 26.58F, 8.62F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(53.57F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(75.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-1.28F, -8.33F, 0.37F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-2.5385F, -9.9904F, 0.4407F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(2.16F, -21.99F, -4.47F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(4.0327F, -26.7952F, -6.4285F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(-14.59F, -21.72F, -1.68F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(7.14F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0417F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.1667F, KeyframeAnimations.degreeVec(33.83F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-90.0F, -7.5F, -0.8F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-90.0F, -7.5F, -0.8F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1.9583F, KeyframeAnimations.posVec(9.0F, 26.0F, -15.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.posVec(11.0F, 26.0F, -16.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(1.9583F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.999F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.2073F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.2083F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(3.0F).looping()
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-0.3F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(18.5F, -9.54F, -4.04F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-10.5F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(18.1921F, 20.5607F, 18.8746F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(17.2807F, 21.3225F, 17.325F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(18.1921F, 20.5607F, 18.8746F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.2136F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(29.9435F, -7.7436F, -22.586F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(30.2136F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(10.9F, 31.72F, 4.9F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-21.86F, -19.88F, 0.05F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition DYING = AnimationDefinition.Builder.withLength(3.0F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(18.1921F, 20.5607F, 18.8746F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-88.9275F, 42.677F, 8.2972F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-108.8448F, 20.1137F, 3.668F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-119.0757F, -45.6173F, 29.3782F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(-119.98F, -45.62F, 29.38F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-2.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(157.6484F, 2.311F, -115.9651F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(157.65F, 2.31F, -115.97F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(-1.0F, -3.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.9157F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.9167F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-150.7524F, -11.3161F, 11.8461F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-87.4615F, -9.9904F, 178.5593F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(-87.46F, -9.99F, 178.56F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(5.0F, 19.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(8.0F, 11.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.8333F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.874F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -7.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-32.4F, 20.3F, -4.02F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-23.4322F, -32.1123F, 16.5904F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-3.0284F, 26.435F, -2.778F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.3088F, 26.4308F, -1.9411F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-4.14F, -9.54F, -4.04F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(12.1819F, 14.6179F, -2.7397F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(5.0133F, -21.9384F, 9.1284F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(17.5133F, -21.9384F, 9.1284F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-14.6198F, 7.2598F, -1.888F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-29.3894F, -15.3709F, -19.928F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(-29.9499F, -13.8323F, -17.6868F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.2136F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(12.02F, -20.35F, -42.05F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(12.02F, -20.35F, -42.05F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-65.3369F, 10.1355F, -37.6327F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(-65.34F, 10.14F, -37.63F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-34.8161F, 14.9084F, 20.3018F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(-34.82F, 14.91F, 20.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-12.0972F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-43.5622F, 29.9239F, 6.9252F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(-83.6598F, 13.3431F, 1.5839F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(-83.66F, 13.34F, 1.58F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-31.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(-31.36F, -19.88F, 0.05F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition CHZZK = AnimationDefinition.Builder.withLength(3.0F).looping()
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.46F, -9.99F, 178.56F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 11.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2907F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -6.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3323F, KeyframeAnimations.posVec(0.0F, -6.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(-1.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.374F, KeyframeAnimations.posVec(-1.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7073F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.749F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(-1.5F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7907F, KeyframeAnimations.posVec(-1.5F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7917F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8323F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.posVec(1.7F, -7.0F, 11.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.874F, KeyframeAnimations.posVec(1.7F, -7.0F, 11.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4157F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4573F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4583F, KeyframeAnimations.posVec(-0.5F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.499F, KeyframeAnimations.posVec(-0.5F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(2.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5407F, KeyframeAnimations.posVec(2.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5417F, KeyframeAnimations.posVec(-2.3F, -7.0F, 11.9F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5823F, KeyframeAnimations.posVec(-2.3F, -7.0F, 11.9F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.749F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.7907F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.7917F, KeyframeAnimations.posVec(-0.5F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.8323F, KeyframeAnimations.posVec(-0.5F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.8333F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.874F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.875F, KeyframeAnimations.posVec(-0.3F, -7.0F, 11.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.9157F, KeyframeAnimations.posVec(-0.3F, -7.0F, 11.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.9167F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6657F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7073F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7083F, KeyframeAnimations.posVec(-0.5F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.749F, KeyframeAnimations.posVec(-0.5F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.75F, KeyframeAnimations.posVec(2.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7907F, KeyframeAnimations.posVec(2.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.posVec(-0.3F, -7.0F, 9.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.8323F, KeyframeAnimations.posVec(-0.3F, -7.0F, 9.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.8333F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.25F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2907F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2917F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3323F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.749F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7907F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7917F, KeyframeAnimations.scaleVec(0.99F, 0.99F, 0.99F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8323F, KeyframeAnimations.scaleVec(0.99F, 0.99F, 0.99F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.scaleVec(1.001F, 1.001F, 1.001F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.874F, KeyframeAnimations.scaleVec(1.001F, 1.001F, 1.001F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.499F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5407F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5823F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5833F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.749F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7907F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.8323F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.8333F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.3088F, 26.4308F, -1.9411F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.5133F, -21.9384F, 9.1284F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-119.98F, -45.62F, 29.38F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-29.9499F, -13.8323F, -17.6868F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(157.65F, 2.31F, -115.97F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-65.34F, 10.14F, -37.63F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-34.82F, 14.91F, 20.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("middle", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(1.2083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-83.66F, 13.34F, 1.58F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-31.36F, -19.88F, 0.05F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition HIDDEN_CHZZK = AnimationDefinition.Builder.withLength(3.0F).looping()
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.46F, -9.99F, 178.56F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 11.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("independent_pickaxe", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2907F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -6.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3323F, KeyframeAnimations.posVec(0.0F, -6.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(-2.0F, -5.0F, 13.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.374F, KeyframeAnimations.posVec(-2.0F, -9.0F, 8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7073F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.749F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(-2.5F, -8.0F, 9.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7907F, KeyframeAnimations.posVec(-2.5F, -7.0F, 13.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7917F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8323F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.posVec(2.7F, -6.0F, 11.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.874F, KeyframeAnimations.posVec(2.7F, -7.0F, 9.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4157F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4573F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4583F, KeyframeAnimations.posVec(-2.5F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.499F, KeyframeAnimations.posVec(-2.5F, -7.0F, 13.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(3.0F, -8.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5407F, KeyframeAnimations.posVec(2.0F, -7.0F, 12.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5417F, KeyframeAnimations.posVec(-3.3F, -7.0F, 8.9F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5823F, KeyframeAnimations.posVec(-2.3F, -5.0F, 11.9F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.749F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.7907F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.7917F, KeyframeAnimations.posVec(-1.5F, -9.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.8323F, KeyframeAnimations.posVec(-2.5F, -7.0F, 9.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.8333F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.874F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.875F, KeyframeAnimations.posVec(-2.3F, -8.0F, 13.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.9157F, KeyframeAnimations.posVec(-2.3F, -7.0F, 11.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.9167F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6657F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7073F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7083F, KeyframeAnimations.posVec(-2.5F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.749F, KeyframeAnimations.posVec(-2.5F, -6.0F, 13.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.75F, KeyframeAnimations.posVec(3.0F, -7.0F, 9.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7907F, KeyframeAnimations.posVec(3.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.posVec(-2.3F, -7.0F, 9.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.8323F, KeyframeAnimations.posVec(-2.3F, -7.0F, 9.3F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.8333F, KeyframeAnimations.posVec(0.0F, -7.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.249F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2907F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2917F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3323F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4157F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.749F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7907F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7917F, KeyframeAnimations.scaleVec(0.99F, 0.99F, 0.99F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8323F, KeyframeAnimations.scaleVec(0.99F, 0.99F, 0.99F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8333F, KeyframeAnimations.scaleVec(1.001F, 1.001F, 1.001F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.874F, KeyframeAnimations.scaleVec(1.001F, 1.001F, 1.001F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.9157F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.9167F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4157F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.499F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5407F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5823F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5833F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.624F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.625F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.749F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.9573F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.9583F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6657F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.749F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7907F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.8323F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.8333F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.874F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.875F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.3088F, 26.4308F, -1.9411F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.5133F, -21.9384F, 9.1284F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-119.98F, -45.62F, 29.38F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-29.9499F, -13.8323F, -17.6868F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(157.65F, 2.31F, -115.97F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-65.34F, 10.14F, -37.63F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-34.82F, 14.91F, 20.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("middle", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(1.2083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-83.66F, 13.34F, 1.58F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-31.36F, -19.88F, 0.05F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}

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
        AnimationMapper.register(AnimationLocations.PHASE_3_IDLE, IDLE);
        AnimationMapper.register(AnimationLocations.STORM, STORM);
        AnimationMapper.register(AnimationLocations.CARPET_BOMBING, CARPET_BOMBING);
        AnimationMapper.register(AnimationLocations.EXPLOSION, EXPLOSION);
    }

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
            .build();

    public static final AnimationDefinition STORM = AnimationDefinition.Builder.withLength(4.0F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-6.36F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(-7.4F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(24.21F, -9.54F, -4.04F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(25.25F, -9.54F, -4.04F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(25.3471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-14.24F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(-14.93F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-15.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.24F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, -0.28F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, -0.3F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(18.1921F, 20.5607F, 18.8746F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-97.03F, -17.54F, 25.08F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-117.6816F, -24.3656F, 26.1981F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(18.1921F, 20.5607F, 18.8746F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-28.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(-32.83F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.2136F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-88.51F, -6.49F, -23.77F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-109.7864F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(30.2136F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-46.31F, 4.76F, 3.67F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(-50.28F, 5.55F, 4.28F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-52.262F, 5.9438F, 4.5822F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(11.72F, 29.67F, 11.44F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(11.94F, 29.33F, 12.53F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(12.0513F, 29.1538F, 13.074F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(23.33F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-31.83F, -11.71F, -5.94F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(-33.57F, -10.35F, -6.94F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-34.4431F, -9.6714F, -7.4337F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(74.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(79.83F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(3.1636F, -43.3125F, 96.8127F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(3.1636F, -43.3125F, 96.8127F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition CARPET_BOMBING = AnimationDefinition.Builder.withLength(4.5F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, 6.25F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-8.1227F, 6.82F, -4.9925F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(21.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(5.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(2.7917F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.posVec(0.0F, -2.54F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(5.5F, 6.52F, -4.03F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(1.3894F, 11.8792F, -4.0246F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(35.61F, -19.0F, -9.07F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(39.1663F, -20.886F, -10.0757F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.degreeVec(40.5371F, 26.2236F, -0.5681F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(52.0649F, 6.0466F, -2.162F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(7.43F, -9.43F, -1.51F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-2.9832F, -9.3205F, 1.0228F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(11.6F, -9.48F, -2.52F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 1.5F, -0.75F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 0.83F, -0.83F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.posVec(0.0F, 1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.posVec(0.0F, 1.71F, -1.71F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 2.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-0.48F, -8.6F, 1.51F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.6961F, -14.802F, 2.0079F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-23.14F, 15.65F, -2.75F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-25.7664F, 16.7779F, -3.3029F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-27.0861F, -23.9274F, 16.3475F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.degreeVec(-27.1F, -13.38F, 10.79F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-27.7297F, -12.7474F, 10.4494F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-1.25F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(7.5F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-4.75F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, -0.83F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(18.1921F, 20.5607F, 18.8746F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(35.89F, 12.08F, 29.27F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(41.7904F, 9.257F, 32.7291F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(23.8F, 12.79F, 39.01F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(24.9244F, 11.2305F, 43.0375F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-76.69F, 11.23F, 43.04F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-87.5756F, 11.2305F, 43.0375F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.degreeVec(-120.3F, 18.66F, 27.52F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-133.3833F, 21.6317F, 21.3084F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-12.7077F, -41.7645F, -20.0678F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-13.96F, -41.76F, -20.07F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-15.2077F, -41.7645F, -20.0678F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-129.33F, 1.86F, 7.19F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(18.1921F, 20.5607F, 18.8746F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.81F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.posVec(0.0F, 1.43F, -0.14F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-21.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-28.75F, -15.56F, 9.32F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-31.5667F, -17.2294F, 10.3141F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.degreeVec(-58.35F, -17.23F, 10.31F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-69.0667F, -17.2294F, 10.3141F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-4.1546F, -10.6944F, -0.1507F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-10.4F, -10.69F, -0.15F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-16.6546F, -10.6944F, -0.1507F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-6.75F, -3.21F, -0.05F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.14F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(-1.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.posVec(-1.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.posVec(-0.3F, 0.0F, -0.3F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.2136F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(28.83F, -11.09F, -15.48F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(28.3705F, -12.6256F, -12.7157F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(51.29F, 5.05F, -38.07F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(55.5034F, 7.3544F, -40.9332F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-89.01F, 7.35F, -40.93F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-104.4966F, 7.3544F, -40.9332F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.degreeVec(-130.49F, -32.14F, -33.92F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-140.8895F, -47.9366F, -31.1136F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-41.2486F, 21.4258F, 15.3056F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-50.0F, 21.43F, 15.31F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-58.7486F, 21.4258F, 15.3056F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-113.98F, 1.88F, -12.04F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(30.2136F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.71F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.posVec(0.0F, 1.43F, -1.57F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.6F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.degreeVec(-61.79F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-77.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-23.2109F, 13.8345F, -5.8546F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-23.2109F, 13.8345F, -5.8546F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-22.71F, 4.15F, -1.76F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.posVec(0.0F, -0.71F, -2.14F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -1.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.posVec(0.0F, -0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-21.39F, 28.01F, -2.58F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-31.9831F, 26.7767F, -5.0739F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(10.4F, 31.72F, 4.9F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.degreeVec(10.4F, 31.72F, 4.9F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(16.4998F, 27.3961F, 12.5072F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(2.9F, 31.72F, 4.9F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-4.5972F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(5.9F, 31.72F, 4.9F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(16.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(9.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-40.11F, -19.88F, 0.05F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-46.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-21.36F, -19.88F, 0.05F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9583F, KeyframeAnimations.degreeVec(-21.36F, -19.88F, 0.05F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-21.0541F, -17.5445F, -0.9091F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5833F, KeyframeAnimations.degreeVec(-41.9212F, -17.9891F, 1.779F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-46.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-28.86F, -19.88F, 0.05F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(59.05F, 5.73F, -4.85F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(64.5675F, 7.6443F, -6.4664F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.9583F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(23.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(38.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(46.36F, 18.23F, -16.7F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(45.1462F, 24.3051F, -22.2691F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(32.6326F, 40.2463F, -45.2559F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(32.6326F, 40.2463F, -45.2559F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5417F, KeyframeAnimations.degreeVec(37.2303F, 57.5713F, -44.2609F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.6667F, KeyframeAnimations.degreeVec(11.07F, 32.25F, -88.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.7917F, KeyframeAnimations.degreeVec(5.4596F, 26.8203F, -97.3773F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.9583F, KeyframeAnimations.degreeVec(7.3549F, -13.3881F, -221.559F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(7.3549F, -13.3881F, -221.559F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5417F, KeyframeAnimations.degreeVec(13.6762F, -6.7948F, -256.5118F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.625F, KeyframeAnimations.degreeVec(13.6762F, -6.7948F, -256.5118F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(11.049F, -10.5667F, -239.2195F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.7917F, KeyframeAnimations.degreeVec(10.57F, -11.03F, -236.72F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(10.0907F, -11.4845F, -234.2119F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.8333F, KeyframeAnimations.degreeVec(-5.6473F, 24.5243F, -271.8743F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.6667F, KeyframeAnimations.posVec(1.81F, -4.52F, -2.71F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.7917F, KeyframeAnimations.posVec(2.0F, -5.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.9583F, KeyframeAnimations.posVec(1.29F, -5.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(1.0F, -5.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition EXPLOSION = AnimationDefinition.Builder.withLength(6.5F)
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -0.22F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -0.31F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.19F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(22.5F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(22.5F, 0.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.25F, KeyframeAnimations.degreeVec(-1.87F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.degreeVec(-4.38F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(-4.38F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(-0.51F, -37.5F, -1.03F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(1.0348F, -52.4955F, -1.441F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(0.28F, -14.0F, -0.38F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, -8.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, -8.83F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -9.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, -9.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -11.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.25F, KeyframeAnimations.posVec(0.0F, 9.25F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.posVec(0.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.posVec(0.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.posVec(0.0F, 3.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-6.98F, 24.27F, -1.31F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-5.765F, 29.9732F, -1.8294F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-24.9948F, -18.8057F, 15.6995F), NarakaInterpolations.FAST_STEP_IN),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-26.5458F, -18.4372F, 17.2267F), NarakaInterpolations.FAST_STEP_OUT),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-14.0458F, -18.4372F, 17.2267F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(-12.65F, -19.39F, 12.94F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-12.3678F, -19.5803F, 12.0781F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-6.12F, -19.58F, 12.08F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.1322F, -19.5803F, 12.0781F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.25F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.125F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(-4.5942F, 25.3662F, -1.6678F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(-7.8861F, 29.9685F, -1.4426F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(-13.82F, -18.93F, 7.86F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(-16.1949F, -38.4905F, 11.577F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(-11.65F, -2.93F, 3.09F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(-10.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(21.06F, -20.74F, -4.01F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(22.3431F, -25.2236F, -3.9957F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(49.3546F, 13.3178F, -0.5701F), NarakaInterpolations.FAST_STEP_IN),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(55.5637F, 15.018F, 2.4857F), NarakaInterpolations.FAST_STEP_OUT),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(55.56F, 15.02F, 2.49F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(45.14F, 15.02F, 2.49F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(43.06F, 15.02F, 2.49F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(45.32F, 15.95F, 1.62F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(47.5803F, 16.8728F, 0.7442F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.25F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.125F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(6.32F, -12.49F, -0.56F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(12.5881F, -14.9854F, -0.6696F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(39.02F, 36.08F, 16.77F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(49.5861F, 56.5012F, 23.7414F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(26.31F, 8.07F, 3.37F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(17.8471F, -9.5447F, -4.0407F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.71F, -1.43F), NarakaInterpolations.FAST_STEP_IN),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 1.0F, -2.0F), NarakaInterpolations.FAST_STEP_OUT),
                    new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 1.0F, -2.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.25F, KeyframeAnimations.posVec(0.0F, 1.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.posVec(0.0F, 1.71F, -0.71F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.posVec(0.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.posVec(0.0F, 0.53F, -0.27F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(18.1921F, 20.5607F, 18.8746F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(20.98F, 18.29F, 9.55F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(22.0954F, 17.3751F, 5.817F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-115.25F, 20.56F, 18.87F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-131.54F, 8.93F, 7.05F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-136.4982F, 5.3968F, 3.4555F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-118.7681F, 6.6045F, 5.6565F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(-122.93F, 6.6F, 5.66F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-123.7681F, 6.6045F, 5.6565F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-130.02F, 6.6F, 5.66F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-136.2681F, 6.6045F, 5.6565F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.degreeVec(17.8981F, 11.9128F, 3.814F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.25F, KeyframeAnimations.degreeVec(-20.32F, 12.73F, 17.37F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.degreeVec(-26.6878F, 12.8676F, 19.6312F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(-14.77F, 31.13F, 41.46F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(-12.3894F, 34.784F, 45.8262F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(-70.54F, -1.72F, 54.98F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(-93.8064F, -16.3222F, 58.6355F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(-11.67F, 10.73F, 29.48F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(18.1921F, 20.5607F, 18.8746F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.83F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, 2.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.25F, KeyframeAnimations.posVec(-0.86F, -0.86F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.posVec(-1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.posVec(-1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.posVec(-3.86F, -1.71F, -0.71F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.posVec(-5.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.posVec(-1.33F, -0.53F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-24.21F, -6.02F, 3.87F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-32.8988F, -8.4215F, 5.4121F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-66.61F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.25F, KeyframeAnimations.degreeVec(-21.41F, 19.91F, -3.75F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.degreeVec(-23.5097F, 24.5948F, -4.6293F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(-23.5097F, 24.5948F, -4.6293F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(-27.35F, 11.37F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(-28.8931F, 6.0803F, 3.2568F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(-9.54F, 1.62F, 0.87F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(3.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.81F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.29F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(22.64F, -34.98F, 53.33F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(11.6921F, -48.9735F, 74.6602F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(11.1452F, -46.5194F, 75.3959F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(38.3614F, -5.5757F, 27.1089F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(38.75F, -9.24F, 24.16F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(38.77F, -9.49F, 23.97F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(38.7737F, -9.4861F, 23.9652F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.25F, KeyframeAnimations.degreeVec(-6.51F, -18.72F, -9.71F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.degreeVec(-21.6111F, -21.8032F, -20.9381F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(8.0152F, -21.7825F, -83.6811F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(-1.1864F, -22.4654F, -78.8492F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(39.7481F, 32.8282F, 24.2681F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(9.4627F, -34.494F, 95.4997F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(-18.7055F, -25.6755F, 112.8261F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.7917F, KeyframeAnimations.degreeVec(-34.9543F, -59.702F, 135.593F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.0417F, KeyframeAnimations.degreeVec(41.7271F, -52.1659F, 26.4596F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("pickaxe", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -0.26F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, -0.37F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.25F, KeyframeAnimations.posVec(13.5F, -5.25F, 6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.posVec(18.0F, -6.0F, 8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.5F, KeyframeAnimations.posVec(34.0F, 11.0F, 8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.posVec(30.0F, 18.0F, 8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.posVec(0.8F, -0.6F, 2.13F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.2136F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(2.08F, -5.19F, -24.52F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-9.1715F, -4.6694F, -24.8173F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(48.24F, 0.2F, -35.08F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(50.82F, 1.16F, -36.69F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(50.8188F, 1.1587F, -36.6929F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(32.7368F, -7.8094F, -67.2948F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(24.8F, -3.35F, -71.69F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(23.21F, -2.4586F, -72.5734F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(23.25F, -0.48F, -77.17F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(23.2873F, 1.4971F, -81.7599F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.degreeVec(21.3411F, -9.9509F, -9.0921F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.25F, KeyframeAnimations.degreeVec(-15.95F, -14.51F, -23.66F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.degreeVec(-22.1677F, -15.2668F, -26.0935F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(-40.27F, -5.59F, -30.01F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(-43.8918F, -3.6525F, -30.7875F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(3.04F, -7.06F, -41.16F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(21.8188F, -8.418F, -45.3098F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(27.98F, -7.0F, -29.51F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(30.2136F, -6.4905F, -23.7661F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.posVec(1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.25F, KeyframeAnimations.posVec(0.86F, -0.86F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.posVec(1.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.posVec(0.27F, -0.27F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-47.72F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-57.81F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-42.19F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.25F, KeyframeAnimations.degreeVec(-28.22F, -23.06F, 7.97F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.degreeVec(-32.5053F, -28.4804F, 9.8491F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(-40.84F, -28.48F, 9.85F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(-42.5053F, -28.4804F, 9.8491F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(-35.36F, -28.48F, 9.85F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(-32.5053F, -28.4804F, 9.8491F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(-25.17F, -7.59F, 2.63F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(3.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.81F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.27F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-25.31F, 31.72F, 4.9F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-39.6F, 31.72F, 4.9F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(8.6653F, 29.414F, 16.2072F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(8.67F, 29.41F, 16.21F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-26.33F, 29.41F, 16.21F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(-22.16F, 29.41F, 16.21F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-21.33F, 29.41F, 16.21F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-33.83F, 29.41F, 16.21F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-46.33F, 29.41F, 16.21F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(-16.92F, 8.87F, 13.47F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(-31.6813F, 12.4236F, 18.8591F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(-0.82F, 26.58F, 8.62F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(10.4028F, 31.721F, 4.8974F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(53.57F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(75.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(95.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(53.57F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(75.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_legdown", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-16.0F, -19.88F, 0.05F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-13.86F, -19.88F, 0.05F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-56.36F, -19.88F, 0.05F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-56.36F, -19.88F, 0.05F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-87.4413F, -27.5039F, 2.3278F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(-91.61F, -27.5F, 2.33F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-92.4413F, -27.5039F, 2.3278F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-97.44F, -27.5F, 2.33F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-102.4413F, -27.5039F, 2.3278F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.25F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(-1.28F, -8.33F, 0.37F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(-2.5385F, -9.9904F, 0.4407F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(2.16F, -21.99F, -4.47F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(4.0327F, -26.7952F, -6.4285F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(-14.59F, -21.72F, -1.68F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(-21.3604F, -19.8753F, 0.0457F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_legdown", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(26.43F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(67.1624F, -9.0548F, 4.2617F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(71.33F, -9.05F, 4.26F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(72.1624F, -9.0548F, 4.2617F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6667F, KeyframeAnimations.degreeVec(74.66F, -9.05F, 4.26F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(77.1624F, -9.0548F, 4.2617F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.875F, KeyframeAnimations.degreeVec(7.14F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.375F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(5.5833F, KeyframeAnimations.degreeVec(33.83F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(6.5F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}

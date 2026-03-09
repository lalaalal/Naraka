package com.yummy.naraka.world.entity.motion;

public class SwordMotionTypes {
    public static final SwordMotion.Builder LONGINUS = SwordMotion.builder()
            .channel(SwordMotionChannel.rotation()
                    .keyframe(0).build()
                    .keyframe(20).value(-Math.PI * 0.8, 0, 0).interpolation(Interpolation.FAST_STEP_IN).build()
                    .keyframe(60).build()
            )
            .channel(SwordMotionChannel.translation()
                    .keyframe(0).build()
                    .keyframe(20).value(0, -3, 4).interpolation(Interpolation.FAST_STEP_IN).build()
                    .keyframe(60).build()
            );
}

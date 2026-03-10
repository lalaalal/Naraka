package com.yummy.naraka.world.entity.motion;

import com.mojang.math.Axis;
import net.minecraft.util.Mth;

public class SwordMotionTypes {
    public static final SwordMotion.Builder LONGINUS = SwordMotion.builder()
            .channel(SwordMotionChannel.rotation(true)
                    .keyframe(MotionKeyframe.rotation(0)
                            .value(Axis.XP.rotation(Mth.PI))
                    )
            )
            .channel(SwordMotionChannel.translation(true)
                    .keyframe(MotionKeyframe.position(0))
            );
}

package com.yummy.naraka.world.entity.motion;

import com.mojang.math.Axis;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.data.MotionData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MotionTypes {
    private static final Map<ResourceLocation, MotionType> REGISTRY = new HashMap<>();

    public static final MotionType EMPTY_TYPE = (positions, rotations) -> Motion.builder()
            .build(NarakaMod.location("empty"));

    public static final ResourceLocation EMPTY = register(NarakaMod.location("empty"), EMPTY_TYPE);

    public static final ResourceLocation SWORD_SWING = register(NarakaMod.location("sword_swing"), Motion.builder()
            .channel(MotionChannel.rotation(true)
                    .keyframe(MotionKeyframe.rotation(0))
                    .keyframe(MotionKeyframe.rotation(40)
                            .value(Axis.XP.rotationDegrees(-90)
                                    .rotateZ(Mth.PI)
                            )
                            .interpolation(Interpolation.Q_FAST_STEP_OUT)
                    )
                    .keyframe(MotionKeyframe.rotation(48)
                            .value(Axis.XP.rotationDegrees(-90)
                                    .rotateZ(-Mth.TWO_PI)
                            )
                    )
                    .keyframe(MotionKeyframe.rotation(50)
                            .value(Axis.XP.rotationDegrees(-90)
                                    .rotateZ(Mth.HALF_PI * 0.5f)
                            )
                    )
                    .keyframe(MotionKeyframe.rotation(75)
                            .value(Axis.XP.rotationDegrees(-180)
                                    .rotateY(Mth.PI)
                            ).interpolation(Interpolation.Q_FAST_STEP_IN)
                    )
                    .keyframe(MotionKeyframe.rotation(85)
                            .value(Axis.XP.rotationDegrees(-180)
                                    .rotateZ(-Mth.HALF_PI * 0.3f)
                            )
                    )
                    .keyframe(MotionKeyframe.rotation(100)
                            .value(Axis.XP.rotationDegrees(-180)
                                    .rotateZ(Mth.PI * 0.6f)
                            )
                            .interpolation(Interpolation.Q_FAST_STEP_IN)
                    )
            )
            .channel(MotionChannel.translation(true)
                    .keyframe(MotionKeyframe.position(0))
                    .keyframe(MotionKeyframe.position(35, 5, -9.3, 0)
                            .interpolation(Interpolation.V_FAST_STEP_OUT)
                    )
                    .keyframe(MotionKeyframe.position(40, 10, -11, 5.5))
                    .keyframe(MotionKeyframe.position(44, 14.5, -11.8, 0))
                    .keyframe(MotionKeyframe.position(48, 9, -12, -5))
                    .keyframe(MotionKeyframe.position(55, 9.5, -15.2, 0.3))
                    .keyframe(MotionKeyframe.position(80, 10.3, -16.2, 0.7))
                    .keyframe(MotionKeyframe.position(85, 9.5, -15, 0.6))
                    .keyframe(MotionKeyframe.position(100, 4.1, -9, 0.2)
                            .interpolation(Interpolation.V_FAST_STEP_IN)
                    )
            )
    );

    public static final ResourceLocation SWORD_STRIKE = register(NarakaMod.location("sword_strike"), Motion.builder()
            .channel(MotionChannel.rotation(true)
                    .keyframe(MotionKeyframe.rotation(0))
                    .keyframe(MotionKeyframe.rotation(40)
                            .value(Axis.YP.rotation(Mth.PI)
                                    .rotateZ(Mth.PI * 0.95f)
                            )
                            .interpolation(Interpolation.Q_FAST_STEP_OUT)
                    )
                    .keyframe(MotionKeyframe.rotation(82)
                            .value(Axis.YP.rotation(Mth.PI)
                                    .rotateZ(Mth.PI * 0.9f)
                            )
                    )
                    .keyframe(MotionKeyframe.rotation(90)
                            .value(Axis.YP.rotation(Mth.PI)
                                    .rotateZ(Mth.TWO_PI * 0.8f)
                            )
                            .interpolation(Interpolation.Q_FAST_STEP_IN)
                    )
            )
            .channel(MotionChannel.translation(true)
                    .keyframe(MotionKeyframe.position(0))
                    .keyframe(MotionKeyframe.position(40, 10.2, -14, 0.3)
                            .interpolation(Interpolation.V_FAST_STEP_OUT))
                    .keyframe(MotionKeyframe.position(82, 10.5, -15.1, 0.3))
                    .keyframe(MotionKeyframe.position(90, 4.3, -9, 0.1)
                            .interpolation(Interpolation.V_FAST_STEP_IN)
                    )
            )
    );

    private static ResourceLocation register(ResourceLocation id, MotionType type) {
        REGISTRY.put(id, type);
        return id;
    }

    private static ResourceLocation register(ResourceLocation id, Motion.Builder builder) {
        return register(id, (positions, rotations) -> builder.build(id));
    }

    public static Motion create(ResourceLocation id) {
        return REGISTRY.getOrDefault(id, EMPTY_TYPE)
                .create(List.of(), List.of());
    }

    public static Motion create(ResourceLocation id, List<Vec3> positions, List<Quaternionf> rotations) {
        return REGISTRY.getOrDefault(id, EMPTY_TYPE)
                .create(positions, rotations);
    }

    public static Motion create(MotionData motionData) {
        return create(motionData.id(), motionData.positions(), motionData.rotations());
    }
}

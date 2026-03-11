package com.yummy.naraka.world.entity.motion;

import com.mojang.math.Axis;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.data.MotionData;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MotionTypes {
    private static final Map<Identifier, MotionType> REGISTRY = new HashMap<>();

    public static final MotionType EMPTY_TYPE = (positions, rotations) -> Motion.builder()
            .channel(MotionChannel.rotation(true)
                    .keyframe(MotionKeyframe.rotation(0))
            )
            .channel(MotionChannel.translation(true)
                    .keyframe(MotionKeyframe.position(0))
            ).build(NarakaMod.identifier("empty"));

    public static final Identifier EMPTY = register(NarakaMod.identifier("empty"), EMPTY_TYPE);

    public static final Identifier SWORD_SWING = register(NarakaMod.identifier("sword_swing"), Motion.builder()
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
                                    .rotateZ(Mth.PI * 0.8f)
                            )
                            .interpolation(Interpolation.Q_FAST_STEP_IN)
                    )
            )
            .channel(MotionChannel.translation(true)
                    .keyframe(MotionKeyframe.position(0))
                    .keyframe(MotionKeyframe.position(35, 1, -0.3, 0)
                            .interpolation(Interpolation.V_FAST_STEP_OUT)
                    )
                    .keyframe(MotionKeyframe.position(40, 2, -1, 1.5))
                    .keyframe(MotionKeyframe.position(44, 3.5, -1.8, 0))
                    .keyframe(MotionKeyframe.position(48, 1, -2, -1))
                    .keyframe(MotionKeyframe.position(55, 1.5, -3.2, 0.3))
                    .keyframe(MotionKeyframe.position(80, 2.3, -3.2, 0.7))
                    .keyframe(MotionKeyframe.position(85, 1.5, -2, 0.6))
                    .keyframe(MotionKeyframe.position(100, 1.1, 0.5, 0.2)
                            .interpolation(Interpolation.V_FAST_STEP_IN)
                    )
            )
    );

    private static Identifier register(Identifier id, MotionType type) {
        REGISTRY.put(id, type);
        return id;
    }

    private static Identifier register(Identifier id, Motion.Builder builder) {
        REGISTRY.put(id, (positions, rotations) -> builder.build(id));
        return id;
    }

    public static Motion create(Identifier id) {
        return REGISTRY.getOrDefault(id, EMPTY_TYPE)
                .create(List.of(), List.of());
    }

    public static Motion create(Identifier id, List<Vec3> positions, List<Quaternionfc> rotations) {
        return REGISTRY.getOrDefault(id, EMPTY_TYPE)
                .create(positions, rotations);
    }

    public static Motion create(MotionData motionData) {
        return create(motionData.id(), motionData.positions(), motionData.rotations());
    }
}

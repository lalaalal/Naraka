package com.yummy.naraka.world.entity.motion;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.List;

@FunctionalInterface
public interface MotionType {
    Motion create(List<Vec3> positions, List<Quaternionf> rotations);

    default Motion create() {
        return create(List.of(), List.of());
    }
}

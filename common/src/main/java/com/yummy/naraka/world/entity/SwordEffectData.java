package com.yummy.naraka.world.entity;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;

public record SwordEffectData(Vec3 head, Vec3 tail, Vec3 base, Vector3fc rotation) {
    public static SwordEffectData of(Vec3 base, Vec3 direction, Vector3fc rotation, float length) {
        Vec3 tail = direction
                .zRot(rotation.z())
                .yRot(rotation.y())
                .xRot(rotation.x());

        Vec3 head = tail.add(direction.normalize().scale(length))
                .zRot(rotation.z())
                .yRot(rotation.y())
                .xRot(rotation.x());
        return new SwordEffectData(head, tail, base, rotation).offset(base);
    }

    public SwordEffectData offset(Vec3 offset) {
        return new SwordEffectData(head.add(offset), tail.add(offset), base, rotation);
    }
}

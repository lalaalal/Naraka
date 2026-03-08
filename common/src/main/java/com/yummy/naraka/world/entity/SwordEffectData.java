package com.yummy.naraka.world.entity;

import net.minecraft.world.phys.Vec3;

public record SwordEffectData(Vec3 head, Vec3 tail) {
    public static SwordEffectData of(Vec3 base, Vec3 direction, float length, float zRot, float yRot) {
        float zRadian = (float) Math.toRadians(zRot);
        float yRadian = (float) Math.toRadians(yRot);
        Vec3 tail = direction.zRot(zRadian).yRot(yRadian);
        Vec3 head = tail.add(direction.normalize().scale(length))
                .zRot(zRadian).yRot(yRadian);
        return new SwordEffectData(head, tail).offset(base);
    }

    public SwordEffectData offset(Vec3 offset) {
        return new SwordEffectData(head.add(offset), tail.add(offset));
    }
}

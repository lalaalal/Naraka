package com.yummy.naraka.world.entity;

import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record SwordEffectData(Vector3fc head, Vector3fc tail, Vector3fc base, Quaternionfc rotation) {
    public static SwordEffectData of(Vector3fc base, Vector3fc direction, Quaternionfc rotation, float length, float scale) {
        Vector3f tail = direction.rotate(rotation, new Vector3f())
                .mul(scale);
        Vector3f head = direction.normalize(length * scale, new Vector3f());
        head.rotate(rotation);

        return new SwordEffectData(head, tail, base, rotation).offset(base);
    }

    public SwordEffectData offset(Vector3fc offset) {
        return new SwordEffectData(head.add(offset, new Vector3f()), tail.add(offset, new Vector3f()), base, rotation);
    }
}

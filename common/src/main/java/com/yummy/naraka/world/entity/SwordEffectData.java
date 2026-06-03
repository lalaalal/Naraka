package com.yummy.naraka.world.entity;

import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record SwordEffectData(Vector3fc base, Vector3fc direction, Quaternionfc rotation, float length, float scale) {
    public Vector3fc head(Vector3fc offset) {
        return direction.rotate(rotation, new Vector3f())
                .mul(scale)
                .add(base)
                .add(offset);
    }

    public Vector3fc tail(Vector3fc offset) {
        return direction.normalize(length * scale, new Vector3f())
                .rotate(rotation)
                .add(base)
                .add(offset);
    }
}

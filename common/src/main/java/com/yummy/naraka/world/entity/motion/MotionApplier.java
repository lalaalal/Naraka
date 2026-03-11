package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.world.entity.Motionable;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public interface MotionApplier<T> {
    static MotionApplier<Vec3> position(boolean relative) {
        return new MotionApplier<>() {
            private Quaternionfc rotation = new Quaternionf();

            @Override
            public Vec3 get(Motionable motionable) {
                rotation = new Quaternionf(motionable.getRotation());
                if (relative)
                    return motionable.getPosition();
                return Vec3.ZERO;
            }

            @Override
            public void apply(Motionable motionable, Vec3 original, Vec3 value) {
                Vector3f target = value.toVector3f().rotate(rotation);
                motionable.setPosition(original.add(new Vec3(target)));
            }
        };
    }

    static MotionApplier<Quaternionfc> rotation(boolean relative) {
        return new MotionApplier<>() {
            @Override
            public Quaternionfc get(Motionable motionable) {
                if (relative)
                    return new Quaternionf(motionable.getRotation());
                return new Quaternionf();
            }

            @Override
            public void apply(Motionable motionable, Quaternionfc original, Quaternionfc value) {
                if (relative)
                    motionable.setRotation(original.mul(value, new Quaternionf()));
                else
                    motionable.setRotation(value);
            }
        };
    }

    T get(Motionable motionable);

    void apply(Motionable motionable, T original, T value);
}

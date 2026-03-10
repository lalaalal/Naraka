package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.world.entity.NarakaSword;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public interface MotionApplier<T> {
    static MotionApplier<Vec3> position(boolean relative) {
        return new MotionApplier<>() {
            private Quaternionfc rotation = new Quaternionf();

            @Override
            public Vec3 get(NarakaSword entity) {
                rotation = new Quaternionf(entity.getRotation());
                if (relative)
                    return entity.position();
                return Vec3.ZERO;
            }

            @Override
            public void apply(NarakaSword entity, Vec3 original, Vec3 value) {
                float scale = entity.getScale();
                Vector3f target = value.scale(scale).toVector3f().rotate(rotation);
                entity.setPos(original.add(new Vec3(target)));
            }
        };
    }

    static MotionApplier<Quaternionfc> rotation(boolean relative) {
        return new MotionApplier<>() {
            @Override
            public Quaternionfc get(NarakaSword entity) {
                if (relative)
                    return new Quaternionf(entity.getRotation());
                return new Quaternionf();
            }

            @Override
            public void apply(NarakaSword entity, Quaternionfc original, Quaternionfc value) {
                if (relative)
                    entity.setRotation(original.mul(value, new Quaternionf()));
                else
                    entity.setRotation(value);
            }
        };
    }

    T get(NarakaSword entity);

    void apply(NarakaSword entity, T original, T value);
}

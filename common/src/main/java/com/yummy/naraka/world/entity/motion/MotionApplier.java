package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.world.entity.NarakaSword;
import net.minecraft.world.phys.Vec3;

public interface MotionApplier {
    MotionApplier POSITION = new MotionApplier() {
        @Override
        public Vec3 get(NarakaSword entity) {
            return entity.position();
        }

        @Override
        public void apply(NarakaSword entity, Vec3 value) {
            entity.setPos(value);
        }
    };
    MotionApplier ROTATION = new MotionApplier() {
        @Override
        public Vec3 get(NarakaSword entity) {
            return new Vec3(entity.getRotation());
        }

        @Override
        public void apply(NarakaSword entity, Vec3 value) {
            entity.setRotation(value.toVector3f());
        }
    };

    Vec3 get(NarakaSword entity);

    void apply(NarakaSword entity, Vec3 value);
}

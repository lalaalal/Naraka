package com.yummy.naraka.world.entity;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;

public interface Motionable {
    Quaternionfc getRotation();

    void setRotation(Quaternionfc rotation);

    Vec3 getPosition();

    void setPosition(Vec3 position);

    float getScale();

    void setScale(float scale);
}

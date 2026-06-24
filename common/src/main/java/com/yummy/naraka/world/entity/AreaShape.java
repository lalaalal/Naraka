package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum AreaShape implements StringRepresentable {
    RECTANGLE,
    CIRCLE;

    public static final Codec<AreaShape> CODEC = StringRepresentable.fromEnum(AreaShape::values);

    @Override
    public String getSerializedName() {
        return name();
    }
}

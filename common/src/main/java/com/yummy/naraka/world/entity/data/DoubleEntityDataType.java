package com.yummy.naraka.world.entity.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class DoubleEntityDataType extends EntityDataType<Double> {
    public DoubleEntityDataType(ResourceLocation id, double defaultValue) {
        super(id, defaultValue);
    }

    @Override
    public Class<Double> getValueType() {
        return Double.class;
    }

    @Override
    public Double read(CompoundTag tag, HolderLookup.Provider provider) {
        return tag.getDoubleOr(name(), getDefaultValue());
    }

    @Override
    public CompoundTag write(Double value, CompoundTag tag, HolderLookup.Provider provider) {
        tag.putDouble(name(), value);
        return tag;
    }
}

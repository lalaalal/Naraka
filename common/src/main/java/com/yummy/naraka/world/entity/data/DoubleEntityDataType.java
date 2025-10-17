package com.yummy.naraka.world.entity.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class DoubleEntityDataType extends EntityDataType<Double> {
    public DoubleEntityDataType(ResourceLocation id, double defaultValue) {
        super(id, defaultValue);
    }

    @Override
    public Class<Double> getValueType() {
        return Double.class;
    }

    @Override
    public Optional<Double> read(CompoundTag tag, HolderLookup.Provider provider) {
        if (tag.contains(name()))
            return Optional.of(tag.getDouble(name()));
        return Optional.empty();
    }

    @Override
    public CompoundTag write(Double value, CompoundTag tag, HolderLookup.Provider provider) {
        tag.putDouble(name(), value);
        return tag;
    }
}

package com.yummy.naraka.world.entity.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class IntegerEntityDataType extends EntityDataType<Integer> {
    public IntegerEntityDataType(ResourceLocation id, int defaultValue) {
        super(id, defaultValue);
    }

    @Override
    public Class<Integer> getValueType() {
        return Integer.class;
    }

    @Override
    public Optional<Integer> read(CompoundTag tag, HolderLookup.Provider provider) {
        if (tag.contains(name()))
            return Optional.of(tag.getInt(name()));
        return Optional.empty();
    }

    @Override
    public CompoundTag write(Integer value, CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt(name(), value);
        return tag;
    }
}

package com.yummy.naraka.world.entity.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class BooleanEntityDataType extends EntityDataType<Boolean> {
    protected BooleanEntityDataType(ResourceLocation id, boolean defaultValue) {
        super(id, defaultValue);
    }

    @Override
    public Class<Boolean> getValueType() {
        return Boolean.class;
    }

    @Override
    public Optional<Boolean> read(CompoundTag tag, HolderLookup.Provider provider) {
        return tag.getBoolean(name());
    }

    @Override
    public CompoundTag write(Boolean value, CompoundTag tag, HolderLookup.Provider provider) {
        tag.putBoolean(name(), value);
        return tag;
    }
}

package com.yummy.naraka.world.entity.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class BooleanEntityDataType extends EntityDataType<Boolean> {
    protected BooleanEntityDataType(ResourceLocation id, boolean defaultValue) {
        super(id, defaultValue);
    }

    @Override
    public Class<Boolean> getValueType() {
        return Boolean.class;
    }

    @Override
    public Boolean read(CompoundTag tag, HolderLookup.Provider provider) {
        return tag.getBooleanOr(name(), getDefaultValue());
    }

    @Override
    public CompoundTag write(Boolean value, CompoundTag tag, HolderLookup.Provider provider) {
        tag.putBoolean(name(), value);
        return tag;
    }
}

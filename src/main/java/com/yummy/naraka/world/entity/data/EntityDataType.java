package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.util.NarakaNbtUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface EntityDataType<T> extends NarakaNbtUtils.TagWriter<T>, NarakaNbtUtils.TagReader<T> {
    ResourceLocation getId();

    default String name() {
        return getId().getPath();
    }

    T getDefaultValue();

    Class<T> getValueType();

    default boolean saveExists(CompoundTag compoundTag) {
        return compoundTag.contains(name());
    }

    default CompoundTag castAndWrite(Object value, CompoundTag tag, HolderLookup.Provider registries) {
        return write(getValueType().cast(value), tag, registries);
    }
}

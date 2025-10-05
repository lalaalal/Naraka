package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.util.NarakaNbtUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public abstract class EntityDataType<T> implements NarakaNbtUtils.TagWriter<T>, NarakaNbtUtils.TagReader<T> {
    private final ResourceLocation id;
    private final Supplier<T> defaultValue;

    protected EntityDataType(ResourceLocation id, T defaultValue) {
        this.id = id;
        this.defaultValue = () -> defaultValue;
    }

    protected EntityDataType(ResourceLocation id, Supplier<T> defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public ResourceLocation getId() {
        return id;
    }

    public T getDefaultValue() {
        return defaultValue.get();
    }

    public String name() {
        return getId().getPath();
    }

    public abstract Class<T> getValueType();

    public T readOrDefault(CompoundTag tag, HolderLookup.Provider provider) {
        return read(tag, provider).orElse(getDefaultValue());
    }

    public boolean saveExists(CompoundTag compoundTag) {
        return compoundTag.contains(name());
    }

    public CompoundTag castAndWrite(Object value, CompoundTag tag, HolderLookup.Provider registries) {
        return write(getValueType().cast(value), tag, registries);
    }
}

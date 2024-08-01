package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class IntegerEntityDataType implements EntityDataType<Integer> {
    private final ResourceLocation id;

    public IntegerEntityDataType(String name) {
        this.id = NarakaMod.location(name);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public Integer getDefaultValue() {
        return 0;
    }

    @Override
    public Class<Integer> getValueType() {
        return Integer.class;
    }

    @Override
    public Integer read(CompoundTag tag, @Nullable HolderLookup.Provider provider) {
        if (tag.contains(name()))
            return tag.getInt(name());
        return getDefaultValue();
    }

    @Override
    public CompoundTag write(Integer value, CompoundTag tag, @Nullable HolderLookup.Provider provider) {
        tag.putInt(name(), value);
        return tag;
    }
}

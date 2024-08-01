package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class StigmaEntityDataType implements EntityDataType<Stigma> {
    public static final ResourceLocation ID = NarakaMod.location("stigma");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Stigma getDefaultValue() {
        return new Stigma();
    }

    @Override
    public Class<Stigma> getValueType() {
        return Stigma.class;
    }

    @Override
    public Stigma read(CompoundTag tag, HolderLookup.@Nullable Provider provider) {
        if (!tag.contains("stigma"))
            return new Stigma();
        int stigma = tag.getInt("stigma");
        long lastMarkedTime = tag.getLong("LastMarkedTime");
        return new Stigma(stigma, lastMarkedTime);
    }

    @Override
    public CompoundTag write(Stigma value, CompoundTag tag, HolderLookup.@Nullable Provider provider) {
        tag.putInt("stigma", value.getStigma());
        tag.putLong("LastMarkedTime", value.getLastMarkedTime());
        return tag;
    }
}

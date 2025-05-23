package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class StigmaEntityDataType extends EntityDataType<Stigma> {
    public static final ResourceLocation ID = NarakaMod.location("stigma");

    protected StigmaEntityDataType() {
        super(ID, Stigma.ZERO);
    }

    @Override
    public Class<Stigma> getValueType() {
        return Stigma.class;
    }

    @Override
    public boolean saveExists(CompoundTag compoundTag) {
        return compoundTag.contains("Stigma") && compoundTag.contains("LastMarkedTime");
    }

    @Override
    public Stigma read(CompoundTag tag, HolderLookup.Provider provider) {
        if (!saveExists(tag))
            return getDefaultValue();
        int stigma = tag.getIntOr("Stigma", 0);
        long lastMarkedTime = tag.getLongOr("LastMarkedTime", 0);
        return new Stigma(stigma, lastMarkedTime);
    }

    @Override
    public CompoundTag write(Stigma value, CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("Stigma", value.value());
        tag.putLong("LastMarkedTime", value.lastMarkedTime());
        return tag;
    }
}

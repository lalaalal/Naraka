package com.yummy.naraka.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public class NarakaNbtUtils {
    public interface TagReader<T> {
        Optional<T> read(CompoundTag tag, HolderLookup.Provider provider);
    }

    public interface TagWriter<T> {
        CompoundTag write(T value, CompoundTag tag, HolderLookup.Provider provider);
    }
}

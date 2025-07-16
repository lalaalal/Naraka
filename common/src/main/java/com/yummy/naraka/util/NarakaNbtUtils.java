package com.yummy.naraka.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NarakaNbtUtils {
    public static final Codec<List<UUID>> UUID_LIST_CODEC = Codec.list(UUIDUtil.CODEC);

    public interface TagReader<T> {
        Optional<T> read(CompoundTag tag, HolderLookup.Provider provider);
    }

    public interface TagWriter<T> {
        CompoundTag write(T value, CompoundTag tag, HolderLookup.Provider provider);
    }
}

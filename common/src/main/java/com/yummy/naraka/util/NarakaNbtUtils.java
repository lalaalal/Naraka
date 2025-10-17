package com.yummy.naraka.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

import java.util.Optional;

public class NarakaNbtUtils {
    public static <T> void store(CompoundTag compoundTag, String key, Codec<T> codec, T value) {
        Tag tag = codec.encodeStart(NbtOps.INSTANCE, value)
                .getOrThrow();
        compoundTag.put(key, tag);
    }

    public static <T> Optional<T> read(CompoundTag compoundTag, String key, Codec<T> codec) {
        Tag tag = compoundTag.get(key);
        DataResult<Pair<T, Tag>> result = codec.decode(NbtOps.INSTANCE, tag);
        if (result.isSuccess())
            return Optional.of(result.getOrThrow().getFirst());
        return Optional.empty();
    }

    public static <T> T readOr(CompoundTag compoundTag, String key, Codec<T> codec, T defaultValue) {
        return read(compoundTag, key, codec).orElse(defaultValue);
    }

    public interface TagReader<T> {
        Optional<T> read(CompoundTag tag, HolderLookup.Provider provider);
    }

    public interface TagWriter<T> {
        CompoundTag write(T value, CompoundTag tag, HolderLookup.Provider provider);
    }
}

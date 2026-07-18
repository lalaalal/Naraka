package com.yummy.naraka.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;

import java.util.Optional;

public class NarakaNbtUtils {
    public static <T> void store(CompoundTag compoundTag, String key, Codec<T> codec, T value) {
        store(compoundTag, key, codec, NbtOps.INSTANCE, value);
    }

    public static <T> void store(CompoundTag compoundTag, String key, Codec<T> codec, DynamicOps<Tag> ops, T value) {
        Tag tag = codec.encodeStart(ops, value)
                .getOrThrow(false, message -> {
                });
        compoundTag.put(key, tag);
    }

    public static <T> void store(CompoundTag compoundTag, String key, Codec<T> codec, HolderLookup.Provider registries, T value) {
        store(compoundTag, key, codec, RegistryOps.create(NbtOps.INSTANCE, registries), value);
    }

    public static <T> Optional<T> read(CompoundTag compoundTag, String key, Codec<T> codec) {
        return read(compoundTag, key, codec, NbtOps.INSTANCE);
    }

    public static <T> Optional<T> read(CompoundTag compoundTag, String key, Codec<T> codec, DynamicOps<Tag> ops) {
        Tag tag = compoundTag.get(key);
        DataResult<Pair<T, Tag>> result = codec.decode(ops, tag);
        return result.result().map(Pair::getFirst);
    }

    public static <T> T readOr(CompoundTag compoundTag, String key, Codec<T> codec, T defaultValue) {
        return read(compoundTag, key, codec, NbtOps.INSTANCE).orElse(defaultValue);
    }

    public static <T> T readOr(CompoundTag compoundTag, String key, Codec<T> codec, DynamicOps<Tag> ops, T defaultValue) {
        return read(compoundTag, key, codec, ops).orElse(defaultValue);
    }
}

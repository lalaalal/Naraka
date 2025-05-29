package com.yummy.naraka.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class NarakaNbtUtils {
    public static CompoundTag writeBlockPos(BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());

        return tag;
    }

    public static Optional<BlockPos> readBlockPos(CompoundTag tag, String name) {
        Optional<CompoundTag> optional = tag.getCompound(name);
        return optional.map(compoundTag -> {
            int x = compoundTag.getIntOr("x", 0);
            int y = compoundTag.getIntOr("y", 0);
            int z = compoundTag.getIntOr("z", 0);
            return new BlockPos(x, y, z);
        });
    }

    public static <T> void writeCollection(CompoundTag compoundTag, String name, Collection<T> collection, TagWriter<T> factory, HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();
        for (T value : collection)
            listTag.add(factory.write(value, new CompoundTag(), provider));
        compoundTag.put(name, listTag);
    }

    public static <T, C extends Collection<T>> C readCollection(CompoundTag compoundTag, String name, Supplier<C> supplier, TagReader<T> tagReader, HolderLookup.Provider provider) {
        ListTag listTag = compoundTag.getListOrEmpty(name);
        C list = supplier.get();
        for (int index = 0; index < listTag.size(); index++) {
            Optional<CompoundTag> valueTag = listTag.getCompound(index);
            if (valueTag.isPresent()) {
                T value = tagReader.read(valueTag.get(), provider);
                list.add(value);
            }
        }
        return list;
    }

    public static CompoundTag writeUUID(UUID uuid, CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putString("UUID", uuid.toString());
        return compoundTag;
    }

    public static UUID readUUID(CompoundTag tag, HolderLookup.Provider provider) {
        Optional<String> uuid = tag.getString("UUID");
        return uuid.map(UUID::fromString)
                .orElseGet(UUID::randomUUID);
    }

    public interface TagReader<T> {
        T read(CompoundTag tag, HolderLookup.Provider provider);
    }

    public interface TagWriter<T> {
        CompoundTag write(T value, CompoundTag tag, HolderLookup.Provider provider);
    }
}

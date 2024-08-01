package com.yummy.naraka.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.*;
import java.util.function.Supplier;

public class NarakaNbtUtils {
    /**
     * Create a new compound tag with {@link ItemStack}
     *
     * @param registries Registry access
     * @param item       Item to save
     * @return New {@link Tag}
     */
    public static Tag writeItem(RegistryAccess registries, ItemStack item) {
        CompoundTag tag = new CompoundTag();
        return item.save(registries, tag);
    }

    /**
     * Read item from given {@link CompoundTag}
     *
     * @param registries RegistryAccess
     * @param tag        Tag of item
     * @return Item
     */
    public static ItemStack readItem(RegistryAccess registries, CompoundTag tag) {
        return ItemStack.parseOptional(registries, tag);
    }

    public static Tag writeBoundingBox(BoundingBox box) {
        CompoundTag boxTag = new CompoundTag();
        BlockPos min = new BlockPos(box.minX(), box.minY(), box.minZ());
        BlockPos max = new BlockPos(box.maxX(), box.maxY(), box.maxZ());
        Tag minTag = NbtUtils.writeBlockPos(min);
        Tag maxTag = NbtUtils.writeBlockPos(max);
        boxTag.put("min", minTag);
        boxTag.put("max", maxTag);

        return boxTag;
    }

    public static Optional<BoundingBox> readBoundingBox(CompoundTag compoundTag, String key) {
        if (!compoundTag.contains(key))
            return Optional.empty();
        CompoundTag boxTag = compoundTag.getCompound(key);
        Optional<BlockPos> min = NbtUtils.readBlockPos(boxTag, "min");
        Optional<BlockPos> max = NbtUtils.readBlockPos(boxTag, "max");
        if (min.isEmpty() || max.isEmpty())
            return Optional.empty();
        return Optional.of(BoundingBox.fromCorners(min.get(), max.get()));
    }

    public interface Factory<T> {
        T create(CompoundTag tag, HolderLookup.Provider provider);
    }

    public interface TagFactory<T> {
        CompoundTag toTag(T value, CompoundTag tag, HolderLookup.Provider provider);
    }

    public static <T> void writeCollection(CompoundTag compoundTag, String name, Collection<T> collection, TagFactory<T> factory, HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();
        for (T value : collection)
            listTag.add(factory.toTag(value, new CompoundTag(), provider));
        compoundTag.put(name, listTag);
    }

    public static <T, C extends Collection<T>> C readCollection(CompoundTag compoundTag, String name, Supplier<C> supplier, Factory<T> factory, HolderLookup.Provider provider) {
        ListTag listTag = compoundTag.getList(name, 10);
        C list = supplier.get();
        for (int index = 0; index < listTag.size(); index++) {
            CompoundTag valueTag = listTag.getCompound(index);
            T value = factory.create(valueTag, provider);
            list.add(value);
        }
        return list;
    }

    public static void writeUUIDs(CompoundTag compoundTag, String name, Collection<UUID> uuids) {
        CompoundTag listTag = new CompoundTag();
        listTag.putInt("size", uuids.size());
        int index = 0;
        for (UUID uuid : uuids) {
            listTag.putUUID(String.valueOf(index), uuid);

            index += 1;
        }
        compoundTag.put(name, listTag);
    }

    public static List<UUID> readUUIDs(CompoundTag compoundTag, String name) {
        if (!compoundTag.contains(name))
            return List.of();
        CompoundTag listTag = compoundTag.getCompound(name);
        int size = listTag.getInt("size");
        List<UUID> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            UUID uuid = listTag.getUUID(String.valueOf(index));
            list.add(uuid);
        }

        return list;
    }

}

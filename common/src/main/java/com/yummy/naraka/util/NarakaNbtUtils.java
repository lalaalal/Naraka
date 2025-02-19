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

import java.util.Collection;
import java.util.Optional;
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

    public static <T> void writeCollection(CompoundTag compoundTag, String name, Collection<T> collection, TagWriter<T> factory, HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();
        for (T value : collection)
            listTag.add(factory.write(value, new CompoundTag(), provider));
        compoundTag.put(name, listTag);
    }

    public static <T, C extends Collection<T>> C readCollection(CompoundTag compoundTag, String name, Supplier<C> supplier, TagReader<T> tagReader, HolderLookup.Provider provider) {
        ListTag listTag = compoundTag.getList(name, 10);
        C list = supplier.get();
        for (int index = 0; index < listTag.size(); index++) {
            CompoundTag valueTag = listTag.getCompound(index);
            T value = tagReader.read(valueTag, provider);
            list.add(value);
        }
        return list;
    }

    public interface TagReader<T> {
        T read(CompoundTag tag, HolderLookup.Provider provider);
    }

    public interface TagWriter<T> {
        CompoundTag write(T value, CompoundTag tag, HolderLookup.Provider provider);
    }
}

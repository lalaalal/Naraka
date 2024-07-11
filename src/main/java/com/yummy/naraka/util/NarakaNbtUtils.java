package com.yummy.naraka.util;

import com.yummy.naraka.event.NarakaGameEventBus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

/**
 * Utils
 *
 * @author lalaalal
 */
public class NarakaNbtUtils {
    private static MinecraftServer server;
    private static final Map<UUID, Entity> cache = new HashMap<>();

    /**
     * Store {@linkplain MinecraftServer}
     *
     * @param server Minecraft server
     * @see NarakaGameEventBus#onServerStarted(ServerStartedEvent)
     */
    public static void initialize(MinecraftServer server) {
        NarakaNbtUtils.server = server;
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
        CompoundTag listTag = new CompoundTag();
        listTag.putInt("size", collection.size());
        int index = 0;
        for (T value : collection) {
            CompoundTag valueTag = factory.toTag(value, new CompoundTag(), provider);
            listTag.put(String.valueOf(index), valueTag);
            index += 1;
        }
        compoundTag.put(name, listTag);
    }

    public static <T> Collection<T> readCollection(CompoundTag compoundTag, String name, Factory<T> factory, Supplier<Collection<T>> supplier, HolderLookup.Provider provider) {
        CompoundTag listTag = compoundTag.getCompound(name);
        int size = listTag.getInt("size");
        Collection<T> list = supplier.get();
        for (int index = 0; index < size; index++) {
            CompoundTag valueTag = listTag.getCompound(String.valueOf(index));
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

    public static @Nullable List<UUID> readUUIDs(CompoundTag compoundTag, String name) {
        if (!compoundTag.contains(name))
            return null;
        CompoundTag listTag = compoundTag.getCompound(name);
        int size = listTag.getInt("size");
        List<UUID> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            UUID uuid = listTag.getUUID(String.valueOf(index));
            list.add(uuid);
        }

        return list;
    }

    public static @Nullable Entity findEntityByUUID(ServerLevel serverLevel, UUID uuid) {
        if (cache.containsKey(uuid))
            return cache.get(uuid);
        for (Entity entity : serverLevel.getAllEntities()) {
            if (entity.getUUID().equals(uuid)) {
                cache.put(uuid, entity);
                return entity;
            }
        }
        return null;
    }

    public static @Nullable <T> T findEntityByUUID(ServerLevel serverLevel, UUID uuid, Class<T> type) {
        Entity entity = findEntityByUUID(serverLevel, uuid);
        if (type.isInstance(entity))
            return type.cast(entity);
        return null;
    }

    /**
     * Search entity using UUID from all levels
     *
     * @param uuid Entity UUID
     * @return Entity matching UUID null if absent
     */
    public static @Nullable Entity findEntityByUUID(UUID uuid) {
        for (ServerLevel serverLevel : server.getAllLevels()) {
            Entity entity = findEntityByUUID(serverLevel, uuid);
            if (entity != null)
                return entity;
        }
        return null;
    }

    /**
     * Search entity using UUID from all levels
     *
     * @param uuid Entity UUID
     * @param type Type to check
     * @param <T>  Type wanted
     * @return Cast to type if type is correct
     * @see NarakaNbtUtilss#findEntityByUUID(ServerLevel, UUID, Class)
     */
    public static @Nullable <T> T findEntityByUUID(UUID uuid, Class<T> type) {
        for (ServerLevel serverLevel : server.getAllLevels()) {
            T entity = findEntityByUUID(serverLevel, uuid, type);
            if (entity != null)
                return entity;
        }
        return null;
    }
}

package com.yummy.naraka;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class NarakaUtil {
    private static String listSizeKey(String baseName) {
        return baseName + "_size";
    }

    private static String elementKey(String baseName, int index) {
        return baseName + "_" + index;
    }

    public static void writeUUIDList(CompoundTag tag, String name, List<UUID> list) {
        tag.putInt(listSizeKey(name), list.size());
        for (int index = 0; index < list.size(); index++) {
            String elementName = elementKey(name, index);
            tag.putUUID(elementName, list.get(index));
        }
    }

    public static @Nullable List<UUID> readUUIDList(CompoundTag tag, String name) {
        if (!tag.contains(listSizeKey(name)))
            return null;
        int size = tag.getInt(name + "_size");
        List<UUID> list = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            String elementName = elementKey(name, index);
            UUID uuid = tag.getUUID(elementName);
            list.add(uuid);
        }

        return list;
    }

    public static void writeEntityReferences(CompoundTag tag, String name, Collection<? extends Entity> list) {
        List<UUID> uuidList = list.stream().map(Entity::getUUID).toList();
        writeUUIDList(tag, name, uuidList);
    }

    public static <T extends Entity> @Nullable List<T> readEntityReferences(CompoundTag tag, String name, ServerLevel serverLevel, Function<Entity, T> typeCaster) {
        List<UUID> uuidList = readUUIDList(tag, name);
        if (uuidList == null)
            return null;
        List<T> entities = new ArrayList<>();

        for (UUID uuid : uuidList) {
            Entity entity = findEntityByUUID(serverLevel, uuid);
            T casted = typeCaster.apply(entity);
            if (casted != null)
                entities.add(casted);
        }

        return entities;
    }

    public static @Nullable Entity findEntityByUUID(ServerLevel level, UUID uuid) {
        for (Entity entity : level.getAllEntities()) {
            if (entity.getUUID().equals(uuid))
                return entity;
        }
        return null;
    }
}

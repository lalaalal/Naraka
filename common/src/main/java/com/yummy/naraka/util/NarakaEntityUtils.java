package com.yummy.naraka.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NarakaEntityUtils {
    private static final Map<UUID, Entity> cache = new HashMap<>();

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
}

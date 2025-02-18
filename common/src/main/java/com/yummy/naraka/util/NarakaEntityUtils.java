package com.yummy.naraka.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NarakaEntityUtils {
    @Nullable
    public static <T> T findEntityByUUID(ServerLevel serverLevel, UUID uuid, Class<T> type) {
        Entity entity = serverLevel.getEntity(uuid);
        if (type.isInstance(entity))
            return type.cast(entity);
        return null;
    }
}

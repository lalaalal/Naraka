package com.yummy.naraka.world.entity.data;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class EntityDataHelper {
    public static void syncEntityData(ServerPlayer player) {
        if (player instanceof EntityDataExtension entityDataExtension)
            entityDataExtension.naraka$syncEntityData(player.serverLevel());
    }

    public static <T, E extends Entity> void setEntityData(E entity, EntityDataType<T, E> entityDataType, T value) {
        if (entity instanceof EntityDataExtension entityDataExtension)
            entityDataExtension.naraka$setEntityData(entityDataType, value);
    }

    public static <T, E extends Entity> T getRawEntityData(E entity, EntityDataType<T, E> entityDataType) {
        if (entity instanceof EntityDataExtension entityDataExtension)
            return entityDataExtension.naraka$getRawEntityData(entityDataType);
        return entityDataType.getDefaultValue();
    }

    public static void removeEntityData(Entity entity, EntityDataType<?, ?> entityDataType) {
        if (entity instanceof EntityDataExtension entityDataExtension)
            entityDataExtension.naraka$removeEntityData(entityDataType);
    }

    public static boolean hasEntityData(Entity entity, EntityDataType<?, ?> entityDataType) {
        if (entity instanceof EntityDataExtension entityDataExtension)
            return entityDataExtension.naraka$hasEntityData(entityDataType);
        return false;
    }
}

package com.yummy.naraka.world.entity.data;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.List;

public interface EntityDataExtension {
    <T, E extends Entity> boolean naraka$hasEntityData(EntityDataType<T, E> entityDataType);

    List<EntityData<?, ?>> naraka$getEntityDataList();

    <T, E extends Entity> EntityData<T, E> naraka$getEntityData(EntityDataType<T, E> entityDataType);

    default <T, E extends Entity> T naraka$getRawEntityData(EntityDataType<T, E> entityDataType) {
        return naraka$getEntityData(entityDataType).value();
    }

    default <T, E extends Entity> void naraka$setEntityData(EntityDataType<T, E> entityDataType, T data) {
        naraka$setEntityData(new EntityData<>(entityDataType, data));
    }

    default <T, E extends Entity> void naraka$setEntityData(EntityData<T, E> entityData) {
        naraka$setEntityData(entityData, true);
    }

    <T, E extends Entity> void naraka$setEntityData(EntityData<T, E> entityData, boolean sync);

    void naraka$loadEntityData(List<EntityData<?, ?>> dataList);

    void naraka$removeEntityData(EntityDataType<?, ?> entityDataType);

    default void naraka$removeEntityData(List<EntityData<?, ?>> entityDataList) {
        for (EntityData<?, ?> entityData : entityDataList)
            naraka$removeEntityData(entityData.type());
    }

    void naraka$syncEntityData(ServerLevel level);
}

package com.yummy.naraka.world.entity.data;

import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class EntityDataContainer {
    private final Map<EntityDataType<?, ?>, EntityData<?, ?>> entityDataMap = new HashMap<>();

    public <T, E extends Entity> void setEntityData(EntityDataType<T, E> entityDataType, T value) {
        this.entityDataMap.put(entityDataType, new EntityData<>(entityDataType, value));
    }

    public void setEntityData(EntityData<?, ?> entityData) {
        this.entityDataMap.put(entityData.type(), entityData);
    }

    public boolean hasEntityData(EntityDataType<?, ?> entityDataType) {
        return entityDataMap.containsKey(entityDataType);
    }

    public <T, E extends Entity> T getRawEntityData(EntityDataType<T, E> entityDataType) {
        return getEntityData(entityDataType).value();
    }

    @SuppressWarnings("unchecked")
    public <T, E extends Entity> EntityData<T, E> getEntityData(EntityDataType<T, E> entityDataType) {
        return (EntityData<T, E>) this.entityDataMap.getOrDefault(entityDataType, entityDataType.getDefault());
    }

    public void removeEntityData(EntityDataType<?, ?> entityDataType) {
        this.entityDataMap.remove(entityDataType);
    }

    public Stream<EntityData<?, ?>> stream() {
        return entityDataMap.values().stream();
    }

    public Set<EntityDataType<?, ?>> getEntityDataTypes() {
        return entityDataMap.keySet();
    }
}

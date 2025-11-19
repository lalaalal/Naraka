package com.yummy.naraka.world.entity.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class EntityDataContainer {
    private final Map<EntityDataType<?>, EntityData<?>> entityDataMap = new HashMap<>();

    public <T> void setEntityData(EntityDataType<T> entityDataType, T entityData) {
        this.entityDataMap.put(entityDataType, new EntityData<>(entityDataType, entityData));
    }

    public boolean hasEntityData(EntityDataType<?> entityDataType) {
        return entityDataMap.containsKey(entityDataType);
    }

    public <T> T getRawEntityData(EntityDataType<T> entityDataType) {
        return getEntityData(entityDataType).value();
    }

    @SuppressWarnings("unchecked")
    public <T> EntityData<T> getEntityData(EntityDataType<T> entityDataType) {
        return (EntityData<T>) this.entityDataMap.getOrDefault(entityDataType, entityDataType.getDefault());
    }

    public Stream<EntityData<?>> stream() {
        return entityDataMap.values().stream();
    }
    
    public Set<EntityDataType<?>> getEntityDataTypes() {
        return entityDataMap.keySet();
    }
}

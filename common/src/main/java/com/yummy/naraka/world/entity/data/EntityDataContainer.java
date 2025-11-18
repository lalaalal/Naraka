package com.yummy.naraka.world.entity.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityDataContainer {
    private final Map<EntityDataType<?>, Object> entityDataMap = new HashMap<>();

    public <T> void setEntityData(EntityDataType<T> entityDataType, T entityData) {
        this.entityDataMap.put(entityDataType, entityData);
    }

    public boolean hasEntityData(EntityDataType<?> entityDataType) {
        return entityDataMap.containsKey(entityDataType);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRawEntityData(EntityDataType<T> entityDataType) {
        if (this.entityDataMap.containsKey(entityDataType))
            return (T) this.entityDataMap.get(entityDataType);
        return entityDataType.getDefaultValue();
    }

    public Set<EntityDataType<?>> getEntityDataTypes() {
        return entityDataMap.keySet();
    }

    public <T> EntityData<T> getEntityData(EntityDataType<T> entityDataType) {
        return new EntityData<>(entityDataType, getRawEntityData(entityDataType));
    }
}

package com.yummy.naraka.world.entity.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

public class EntityDataContainer {
    private final Map<EntityDataType<?>, Object> entityDataMap = new HashMap<>();

    public void setEntityData(EntityDataType<?> entityDataType, Object entityData) {
        this.entityDataMap.put(entityDataType, entityData);
    }

    public <T> T getEntityData(EntityDataType<T> entityDataType) {
        if (this.entityDataMap.containsKey(entityDataType))
            return entityDataType.getValueType().cast(this.entityDataMap.get(entityDataType));
        return entityDataType.getDefaultValue();
    }

    public void save(CompoundTag compoundTag, HolderLookup.Provider registries) {
        for (EntityDataType<?> entityDataType : entityDataMap.keySet())
            entityDataType.castAndWrite(entityDataMap.get(entityDataType), compoundTag, registries);
    }
}

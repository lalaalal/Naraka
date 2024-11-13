package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

public class EntityDataContainer {
    private final Map<EntityDataType<?>, Object> entityDataMap = new HashMap<>();

    public void setEntityData(EntityDataType<?> entityDataType, Object entityData) {
        this.entityDataMap.put(entityDataType, entityData);
    }

    public boolean hasEntityData(EntityDataType<?> entityDataType) {
        return entityDataMap.containsKey(entityDataType);
    }

    public <T> T getEntityData(EntityDataType<T> entityDataType) {
        if (this.entityDataMap.containsKey(entityDataType))
            return entityDataType.getValueType().cast(this.entityDataMap.get(entityDataType));
        return entityDataType.getDefaultValue();
    }

    public void save(CompoundTag compoundTag, HolderLookup.Provider registries) {
        for (EntityDataType<?> entityDataType : entityDataMap.keySet()) {
            if (entityDataMap.get(entityDataType) != entityDataType.getDefaultValue())
                entityDataType.castAndWrite(entityDataMap.get(entityDataType), compoundTag, registries);
        }
    }

    public void save(CompoundTag compoundTag, HolderLookup.Provider registries, HolderSet<EntityDataType<?>> entityDataTypes) {
        for (Holder<EntityDataType<?>> holder : entityDataTypes) {
            Object value = getEntityData(holder.value());
            holder.value().castAndWrite(value, compoundTag, registries);
        }
    }

    public void read(CompoundTag compoundTag, HolderLookup.Provider registries) {
        NarakaRegistries.ENTITY_DATA_TYPE.stream().toList()
                .forEach(entityDataType -> {
                    if (entityDataType.saveExists(compoundTag)) {
                        Object data = entityDataType.read(compoundTag, registries);
                        setEntityData(entityDataType, data);
                    }
                });
    }
}

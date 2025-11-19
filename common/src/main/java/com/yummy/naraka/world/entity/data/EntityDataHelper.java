package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;

public class EntityDataHelper {
    private static final Map<UUID, EntityDataContainer> ENTITY_DATA_MAP = new HashMap<>();
    private static final Map<EntityDataType<?>, List<DataChangeListener<?>>> DATA_CHANGE_LISTENERS = new HashMap<>();

    public static void clear() {
        ENTITY_DATA_MAP.clear();
    }

    public static <T> void registerDataChangeListener(EntityDataType<T> entityDataType, DataChangeListener<T> listener) {
        DATA_CHANGE_LISTENERS.computeIfAbsent(entityDataType, _entityDataType -> new ArrayList<>())
                .add(listener);
    }

    public static void syncEntityData(LivingEntity livingEntity, EntityDataType<?> entityDataType) {
        if (livingEntity.level() instanceof ServerLevel serverLevel && serverLevel.getServer().isDedicatedServer()) {
            EntityData<?> data = getEntityData(livingEntity, entityDataType);
            for (ServerPlayer player : serverLevel.players())
                NetworkManager.clientbound().send(player, new SyncEntityDataPacket(livingEntity, data));
        }
    }

    public static void syncEntityData(LivingEntity livingEntity) {
        if (livingEntity.level() instanceof ServerLevel serverLevel && serverLevel.getServer().isDedicatedServer()) {
            List<EntityData<?>> data = getEntityDataList(livingEntity);
            for (ServerPlayer player : serverLevel.players())
                NetworkManager.clientbound().send(player, new SyncEntityDataPacket(livingEntity, data));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void setEntityData(LivingEntity livingEntity, EntityDataType<T> entityDataType, T value) {
        EntityDataContainer container = ENTITY_DATA_MAP.computeIfAbsent(livingEntity.getUUID(), uuid -> new EntityDataContainer());
        T original = container.getRawEntityData(entityDataType);
        container.setEntityData(entityDataType, value);
        syncEntityData(livingEntity, entityDataType);
        for (DataChangeListener<?> listener : DATA_CHANGE_LISTENERS.computeIfAbsent(entityDataType, type -> new ArrayList<>())) {
            DataChangeListener<T> castedListener = (DataChangeListener<T>) listener;
            castedListener.onChange(livingEntity, entityDataType, original, value);
        }
    }

    public static <T> void setEntityData(LivingEntity livingEntity, EntityData<T> entityData) {
        setEntityData(livingEntity, entityData.type(), entityData.value());
    }

    public static void setEntityDataList(LivingEntity livingEntity, List<EntityData<?>> entityDataList) {
        entityDataList.forEach(entityData -> setEntityData(livingEntity, entityData));
    }

    public static <T> T getRawEntityData(LivingEntity livingEntity, EntityDataType<T> entityDataType) {
        if (!ENTITY_DATA_MAP.containsKey(livingEntity.getUUID()))
            return entityDataType.getDefaultValue();
        EntityDataContainer container = ENTITY_DATA_MAP.get(livingEntity.getUUID());
        return container.getRawEntityData(entityDataType);
    }

    public static <T> EntityData<T> getEntityData(LivingEntity livingEntity, EntityDataType<T> entityDataType) {
        if (!ENTITY_DATA_MAP.containsKey(livingEntity.getUUID()))
            return entityDataType.getDefault();
        EntityDataContainer container = ENTITY_DATA_MAP.get(livingEntity.getUUID());
        return container.getEntityData(entityDataType);
    }

    public static Set<EntityDataType<?>> getEntityDataTypes(LivingEntity livingEntity) {
        if (!ENTITY_DATA_MAP.containsKey(livingEntity.getUUID()))
            return Set.of();
        EntityDataContainer container = ENTITY_DATA_MAP.get(livingEntity.getUUID());
        return container.getEntityDataTypes();
    }

    public static List<EntityData<?>> getEntityDataList(LivingEntity livingEntity) {
        if (!ENTITY_DATA_MAP.containsKey(livingEntity.getUUID()))
            return List.of();
        EntityDataContainer container = ENTITY_DATA_MAP.get(livingEntity.getUUID());
        return container.stream().toList();
    }

    public static void removeEntityData(LivingEntity entity) {
        ENTITY_DATA_MAP.remove(entity.getUUID());
        syncEntityData(entity);
    }

    public static boolean hasEntityData(LivingEntity livingEntity) {
        return ENTITY_DATA_MAP.containsKey(livingEntity.getUUID());
    }

    public static boolean hasEntityData(LivingEntity livingEntity, EntityDataType<?> entityDataType) {
        if (hasEntityData(livingEntity)) {
            return ENTITY_DATA_MAP.get(livingEntity.getUUID())
                    .hasEntityData(entityDataType);
        }
        return false;
    }

    public interface DataChangeListener<T> {
        void onChange(LivingEntity livingEntity, EntityDataType<T> entityDataType, T from, T to);
    }
}

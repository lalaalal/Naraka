package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;

public class EntityDataHelper {
    private static final Map<UUID, EntityDataContainer> ENTITY_DATA_MAP = new HashMap<>();
    private static final Map<EntityDataType<?, ?>, List<DataChangeListener<?, ?>>> DATA_CHANGE_LISTENERS = new HashMap<>();

    public static void clear() {
        ENTITY_DATA_MAP.clear();
    }

    public static <T, E extends Entity> void registerDataChangeListener(EntityDataType<T, E> entityDataType, DataChangeListener<T, E> listener) {
        DATA_CHANGE_LISTENERS.computeIfAbsent(entityDataType, _entityDataType -> new ArrayList<>())
                .add(listener);
    }

    public static void syncEntityData(Entity entity, EntityDataType<?, ?> entityDataType, SyncEntityDataPacket.Action action) {
        if (entity.level() instanceof ServerLevel serverLevel && serverLevel.getServer().isDedicatedServer()) {
            EntityData<?, ?> data = getEntityData(entity, entityDataType);
            for (ServerPlayer player : serverLevel.players())
                NetworkManager.clientbound().send(player, SyncEntityDataPacket.sync(entity, action, data));
        }
    }

    public static void syncEntityData(Entity entity, SyncEntityDataPacket.Action action) {
        if (entity.level() instanceof ServerLevel serverLevel && serverLevel.getServer().isDedicatedServer()) {
            List<EntityData<?, ?>> data = getEntityDataList(entity);
            for (ServerPlayer player : serverLevel.players())
                NetworkManager.clientbound().send(player, SyncEntityDataPacket.sync(entity, action, data));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, E extends Entity> void setEntityData(E entity, EntityDataType<T, E> entityDataType, T value) {
        EntityDataContainer container = ENTITY_DATA_MAP.computeIfAbsent(entity.getUUID(), uuid -> new EntityDataContainer());
        T original = container.getRawEntityData(entityDataType);
        container.setEntityData(entityDataType, value);
        syncEntityData(entity, entityDataType, SyncEntityDataPacket.Action.LOAD);
        for (DataChangeListener<?, ?> listener : DATA_CHANGE_LISTENERS.computeIfAbsent(entityDataType, type -> new ArrayList<>())) {
            DataChangeListener<T, E> castedListener = (DataChangeListener<T, E>) listener;
            castedListener.onChange(entity, entityDataType, original, value);
        }
    }

    public static void loadEntityData(Entity entity, EntityData<?, ?> entityData) {
        ENTITY_DATA_MAP.computeIfAbsent(entity.getUUID(), uuid -> new EntityDataContainer())
                .setEntityData(entityData);
        syncEntityData(entity, entityData.type(), SyncEntityDataPacket.Action.LOAD);
    }

    public static void loadEntityDataList(Entity entity, List<EntityData<?, ?>> entityDataList) {
        entityDataList.forEach(entityData -> loadEntityData(entity, entityData));
    }

    public static <T, E extends Entity> T getRawEntityData(E entity, EntityDataType<T, E> entityDataType) {
        if (!ENTITY_DATA_MAP.containsKey(entity.getUUID()))
            return entityDataType.getDefaultValue();
        EntityDataContainer container = ENTITY_DATA_MAP.get(entity.getUUID());
        return container.getRawEntityData(entityDataType);
    }

    public static <T, E extends Entity> EntityData<T, ? extends E> getEntityData(E entity, EntityDataType<T, ? extends E> entityDataType) {
        if (!ENTITY_DATA_MAP.containsKey(entity.getUUID()))
            return entityDataType.getDefault();
        EntityDataContainer container = ENTITY_DATA_MAP.get(entity.getUUID());
        return container.getEntityData(entityDataType);
    }

    public static Set<EntityDataType<?, ?>> getEntityDataTypes(Entity entity) {
        if (!ENTITY_DATA_MAP.containsKey(entity.getUUID()))
            return Set.of();
        EntityDataContainer container = ENTITY_DATA_MAP.get(entity.getUUID());
        return container.getEntityDataTypes();
    }

    public static List<EntityData<?, ?>> getEntityDataList(Entity entity) {
        if (!ENTITY_DATA_MAP.containsKey(entity.getUUID()))
            return List.of();
        EntityDataContainer container = ENTITY_DATA_MAP.get(entity.getUUID());
        return container.stream().toList();
    }

    public static void removeEntityData(UUID uuid) {
        ENTITY_DATA_MAP.remove(uuid);
    }

    public static void removeEntityData(Entity entity) {
        ENTITY_DATA_MAP.remove(entity.getUUID());
        syncEntityData(entity, SyncEntityDataPacket.Action.REMOVE_ALL);
    }

    public static void removeEntityData(Entity entity, EntityDataType<?, ?> entityDataType) {
        if (ENTITY_DATA_MAP.containsKey(entity.getUUID())) {
            EntityDataContainer container = ENTITY_DATA_MAP.get(entity.getUUID());
            container.removeEntityData(entityDataType);
        }
        syncEntityData(entity, SyncEntityDataPacket.Action.REMOVE_GIVEN);
    }

    public static boolean hasEntityData(Entity entity) {
        return ENTITY_DATA_MAP.containsKey(entity.getUUID());
    }

    public static boolean hasEntityData(LivingEntity livingEntity, EntityDataType<?, ?> entityDataType) {
        if (hasEntityData(livingEntity)) {
            return ENTITY_DATA_MAP.get(livingEntity.getUUID())
                    .hasEntityData(entityDataType);
        }
        return false;
    }

    public interface DataChangeListener<T, E extends Entity> {
        void onChange(E entity, EntityDataType<T, E> entityDataType, T from, T to);
    }
}

package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.*;

public class EntityDataHelper {
    private static final Map<Containerkey, Map<UUID, EntityDataContainer>> LEVEL_ENTITY_DATA_MAP = new HashMap<>();
    private static final Map<EntityDataType<?, ?>, List<DataChangeListener<?, ?>>> DATA_CHANGE_LISTENERS = new HashMap<>();

    public static void clear() {
        LEVEL_ENTITY_DATA_MAP.clear();
    }

    private static Map<UUID, EntityDataContainer> getEntityDataMap(Level level) {
        return LEVEL_ENTITY_DATA_MAP.computeIfAbsent(Containerkey.of(level), _level -> new HashMap<>());
    }

    public static <T, E extends Entity> void registerDataChangeListener(EntityDataType<T, E> entityDataType, DataChangeListener<T, E> listener) {
        DATA_CHANGE_LISTENERS.computeIfAbsent(entityDataType, _entityDataType -> new ArrayList<>())
                .add(listener);
    }

    public static void syncEntityData(Entity entity, EntityDataType<?, ?> entityDataType, SyncEntityDataPacket.Action action) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            EntityData<?, ?> data = getEntityData(entity, entityDataType);
            for (ServerPlayer player : serverLevel.players())
                NetworkManager.clientbound().send(player, SyncEntityDataPacket.sync(entity, action, data));
        }
    }

    public static void syncEntityData(Entity entity, SyncEntityDataPacket.Action action) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            List<EntityData<?, ?>> data = getEntityDataList(entity);
            for (ServerPlayer player : serverLevel.players())
                NetworkManager.clientbound().send(player, SyncEntityDataPacket.sync(entity, action, data));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, E extends Entity> void setEntityData(E entity, EntityDataType<T, E> entityDataType, T value) {
        Map<UUID, EntityDataContainer> entityDataContainers = getEntityDataMap(entity.level());
        EntityDataContainer container = entityDataContainers.computeIfAbsent(entity.getUUID(), uuid -> new EntityDataContainer());
        T original = container.getRawEntityData(entityDataType);
        container.setEntityData(entityDataType, value);
        syncEntityData(entity, entityDataType, SyncEntityDataPacket.Action.LOAD);
        for (DataChangeListener<?, ?> listener : DATA_CHANGE_LISTENERS.computeIfAbsent(entityDataType, type -> new ArrayList<>())) {
            DataChangeListener<T, E> castedListener = (DataChangeListener<T, E>) listener;
            castedListener.onChange(entity, entityDataType, original, value);
        }
    }

    public static void loadEntityData(Entity entity, EntityData<?, ?> entityData) {
        loadEntityData(entity.level(), entity.getUUID(), entityData);
        syncEntityData(entity, entityData.type(), SyncEntityDataPacket.Action.LOAD);
    }

    public static void loadEntityData(Level level, UUID uuid, EntityData<?, ?> entityData) {
        Map<UUID, EntityDataContainer> entityDataContainers = getEntityDataMap(level);
        entityDataContainers.computeIfAbsent(uuid, _uuid -> new EntityDataContainer())
                .setEntityData(entityData);
    }

    public static void loadEntityDataList(Entity entity, List<EntityData<?, ?>> entityDataList) {
        entityDataList.forEach(entityData -> loadEntityData(entity, entityData));
    }

    public static <T, E extends Entity> T getRawEntityData(E entity, EntityDataType<T, E> entityDataType) {
        Map<UUID, EntityDataContainer> entityDataContainers = getEntityDataMap(entity.level());
        if (!entityDataContainers.containsKey(entity.getUUID()))
            return entityDataType.getDefaultValue();
        EntityDataContainer container = entityDataContainers.get(entity.getUUID());
        return container.getRawEntityData(entityDataType);
    }

    public static <T, E extends Entity> EntityData<T, ? extends E> getEntityData(E entity, EntityDataType<T, ? extends E> entityDataType) {
        Map<UUID, EntityDataContainer> entityDataContainers = getEntityDataMap(entity.level());
        if (!entityDataContainers.containsKey(entity.getUUID()))
            return entityDataType.getDefault();
        EntityDataContainer container = entityDataContainers.get(entity.getUUID());
        return container.getEntityData(entityDataType);
    }

    public static Set<EntityDataType<?, ?>> getEntityDataTypes(Entity entity) {
        Map<UUID, EntityDataContainer> entityDataContainers = getEntityDataMap(entity.level());
        if (!entityDataContainers.containsKey(entity.getUUID()))
            return Set.of();
        EntityDataContainer container = entityDataContainers.get(entity.getUUID());
        return container.getEntityDataTypes();
    }

    public static List<EntityData<?, ?>> getEntityDataList(Entity entity) {
        Map<UUID, EntityDataContainer> entityDataContainers = getEntityDataMap(entity.level());
        if (!entityDataContainers.containsKey(entity.getUUID()))
            return List.of();
        EntityDataContainer container = entityDataContainers.get(entity.getUUID());
        return container.stream().toList();
    }

    public static void removeEntityData(Level level, UUID uuid) {
        Map<UUID, EntityDataContainer> entityDataContainers = getEntityDataMap(level);
        entityDataContainers.remove(uuid);
    }

    public static void removeEntityData(Entity entity) {
        removeEntityData(entity.level(), entity.getUUID());
        syncEntityData(entity, SyncEntityDataPacket.Action.REMOVE_ALL);
    }

    public static void removeEntityData(Level level, UUID uuid, EntityDataType<?, ?> entityDataType) {
        Map<UUID, EntityDataContainer> entityDataContainers = getEntityDataMap(level);
        if (entityDataContainers.containsKey(uuid)) {
            EntityDataContainer container = entityDataContainers.get(uuid);
            container.removeEntityData(entityDataType);
        }
    }

    public static void removeEntityData(Entity entity, EntityDataType<?, ?> entityDataType) {
        removeEntityData(entity.level(), entity.getUUID(), entityDataType);
        syncEntityData(entity, SyncEntityDataPacket.Action.REMOVE_GIVEN);
    }

    public static boolean hasEntityData(Entity entity) {
        Map<UUID, EntityDataContainer> entityDataContainers = getEntityDataMap(entity.level());
        return entityDataContainers.containsKey(entity.getUUID());
    }

    public static boolean hasEntityData(Entity entity, EntityDataType<?, ?> entityDataType) {
        if (hasEntityData(entity)) {
            Map<UUID, EntityDataContainer> entityDataContainers = getEntityDataMap(entity.level());
            return entityDataContainers.get(entity.getUUID())
                    .hasEntityData(entityDataType);
        }
        return false;
    }

    public interface DataChangeListener<T, E extends Entity> {
        void onChange(E entity, EntityDataType<T, E> entityDataType, T from, T to);
    }

    private record Containerkey(ResourceKey<Level> dimension, boolean isClient) {
        public static Containerkey of(Level level) {
            return new Containerkey(level.dimension(), level.isClientSide());
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Containerkey(ResourceKey<Level> _dimension, boolean client))) return false;
            return isClient == client && dimension.equals(_dimension);
        }

        @Override
        public int hashCode() {
            int result = dimension.hashCode();
            result = 31 * result + Boolean.hashCode(isClient);
            return result;
        }
    }
}

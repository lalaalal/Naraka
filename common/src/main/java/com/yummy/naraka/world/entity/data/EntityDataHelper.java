package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.network.SyncEntityDataPayload;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;
import java.util.function.Supplier;

public class EntityDataHelper {
    private static final Map<UUID, EntityDataContainer> ENTITY_DATA_MAP = new HashMap<>();
    private static final Map<Supplier<? extends EntityDataType<?>>, List<WrapperListener>> DATA_CHANGE_LISTENERS = new HashMap<>();

    public static void clear() {
        ENTITY_DATA_MAP.clear();
    }

    public static <T> void registerDataChangeListener(Supplier<? extends EntityDataType<T>> entityDataType, DataChangeListener<T> listener) {
        List<WrapperListener> listeners = DATA_CHANGE_LISTENERS.computeIfAbsent(entityDataType, _entityDataType -> new ArrayList<>());
        listeners.add(wrap(entityDataType, listener));
    }

    private static <T> WrapperListener wrap(Supplier<? extends EntityDataType<T>> entityDataType, DataChangeListener<T> listener) {
        return (livingEntity, _entityDataType, from, to) -> {
            Class<T> type = entityDataType.get().getValueType();
            listener.onChange(livingEntity, entityDataType.get(), type.cast(from), type.cast(to));
        };
    }

    public static void syncEntityData(LivingEntity livingEntity, Supplier<? extends EntityDataType<?>> entityDataType) {
        Holder<EntityDataType<?>> holder = NarakaEntityDataTypes.holder(livingEntity.registryAccess(), entityDataType.get());
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            CompoundTag data = new CompoundTag();
            saveEntityData(livingEntity, data);
            for (ServerPlayer player : serverLevel.players())
                NetworkManager.sendToPlayer(player, new SyncEntityDataPayload(livingEntity, holder, data));
        }
    }

    public static void syncEntityData(LivingEntity livingEntity) {
        HolderSet<EntityDataType<?>> holders = NarakaEntityDataTypes.full(livingEntity.registryAccess());
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            CompoundTag data = new CompoundTag();
            saveEntityData(livingEntity, data, holders);
            for (ServerPlayer player : serverLevel.players())
                NetworkManager.sendToPlayer(player, new SyncEntityDataPayload(livingEntity, holders, data));
        }
    }

    public static void setEntityData(LivingEntity livingEntity, Supplier<? extends EntityDataType<?>> entityDataType, Object value) {
        EntityDataContainer container = ENTITY_DATA_MAP.computeIfAbsent(livingEntity.getUUID(), uuid -> new EntityDataContainer());
        Object original = container.getEntityData(entityDataType.get());
        container.setEntityData(entityDataType.get(), value);
        syncEntityData(livingEntity, entityDataType);
        for (WrapperListener listener : DATA_CHANGE_LISTENERS.computeIfAbsent(entityDataType, type -> new ArrayList<>()))
            listener.onChange(livingEntity, entityDataType.get(), original, value);
    }

    public static <T> T getEntityData(LivingEntity livingEntity, Supplier<? extends EntityDataType<T>> entityDataType) {
        if (!ENTITY_DATA_MAP.containsKey(livingEntity.getUUID()))
            return entityDataType.get().getDefaultValue();
        EntityDataContainer container = ENTITY_DATA_MAP.get(livingEntity.getUUID());
        return container.getEntityData(entityDataType.get());
    }

    public static void removeEntityData(LivingEntity entity) {
        ENTITY_DATA_MAP.remove(entity.getUUID());
    }

    public static void saveEntityData(LivingEntity livingEntity, CompoundTag compoundTag) {
        RegistryAccess registries = livingEntity.level().registryAccess();
        EntityDataContainer container = ENTITY_DATA_MAP.get(livingEntity.getUUID());
        if (container != null)
            container.save(compoundTag, registries);
    }

    public static void saveEntityData(LivingEntity livingEntity, CompoundTag compoundTag, HolderSet<EntityDataType<?>> entityDataTypes) {
        RegistryAccess registries = livingEntity.level().registryAccess();
        EntityDataContainer container = ENTITY_DATA_MAP.get(livingEntity.getUUID());
        if (container != null)
            container.save(compoundTag, registries, entityDataTypes);
    }

    public static void readEntityData(LivingEntity livingEntity, CompoundTag compoundTag) {
        RegistryAccess registries = livingEntity.level().registryAccess();
        EntityDataContainer container = new EntityDataContainer();
        container.read(compoundTag, registries);
        ENTITY_DATA_MAP.put(livingEntity.getUUID(), container);
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

    private interface WrapperListener {
        void onChange(LivingEntity livingEntity, EntityDataType<?> entityDataType, Object from, Object to);
    }

    public interface DataChangeListener<T> {
        void onChange(LivingEntity livingEntity, EntityDataType<T> entityDataType, T from, T to);
    }
}

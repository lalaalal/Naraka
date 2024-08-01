package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.network.SyncEntityDataPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class EntityDataHelper {
    private static final Map<LivingEntity, EntityDataContainer> ENTITY_DATA_MAP = new HashMap<>();

    public static void syncEntityData(LivingEntity livingEntity, EntityDataType<?> entityDataType) {
        Holder<EntityDataType<?>> holder = EntityDataTypes.holder(entityDataType);
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            CompoundTag data = new CompoundTag();
            saveEntityData(livingEntity, data);
            for (ServerPlayer player : serverLevel.players())
                ServerPlayNetworking.send(player, new SyncEntityDataPayload(livingEntity, holder, data));
        }
    }

    public static void setEntityData(LivingEntity livingEntity, EntityDataType<?> entityDataType, Object value) {
        EntityDataContainer container = ENTITY_DATA_MAP.computeIfAbsent(livingEntity, e -> new EntityDataContainer());
        container.setEntityData(entityDataType, value);
        syncEntityData(livingEntity, entityDataType);
    }

    public static <T> T getEntityData(LivingEntity entity, EntityDataType<T> entityDataType) {
        if (!ENTITY_DATA_MAP.containsKey(entity))
            return entityDataType.getDefaultValue();
        EntityDataContainer container = ENTITY_DATA_MAP.get(entity);
        return container.getEntityData(entityDataType);
    }

    public static void removeEntityData(LivingEntity entity) {
        ENTITY_DATA_MAP.remove(entity);
    }

    public static void saveEntityData(LivingEntity entity, CompoundTag compoundTag) {
        RegistryAccess registries = entity.level().registryAccess();
        EntityDataContainer container = ENTITY_DATA_MAP.get(entity);
        if (container != null)
            container.save(compoundTag, registries);
    }

    public static void readEntityData(LivingEntity entity, CompoundTag compoundTag) {
        RegistryAccess registries = entity.level().registryAccess();
        for (EntityDataType<?> entityDataType : NarakaRegistries.ENTITY_DATA_TYPE) {
            if (entityDataType.saveExists(compoundTag)) {
                EntityDataContainer container = ENTITY_DATA_MAP.computeIfAbsent(entity, e -> new EntityDataContainer());
                Object data = entityDataType.read(compoundTag, registries);
                container.setEntityData(entityDataType, data);
            }
        }
    }

    public static boolean hasEntityData(LivingEntity entity) {
        return ENTITY_DATA_MAP.containsKey(entity);
    }
}

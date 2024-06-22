package com.yummy.naraka.world.entity;

import net.minecraft.world.entity.LivingEntity;

import java.util.Set;
import java.util.UUID;

public interface DeathCountingEntity {
    Set<UUID> getDeathCountedEntities();

    void onDeathCountZero(LivingEntity livingEntity);

    default boolean isDeathCounting(LivingEntity livingEntity) {
        return getDeathCountedEntities().contains(livingEntity.getUUID());
    }
}

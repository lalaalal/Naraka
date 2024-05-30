package com.yummy.naraka.entity;

import net.minecraft.world.entity.LivingEntity;

import java.util.Set;
import java.util.UUID;

public interface DeathCountingEntity {
    Set<LivingEntity> getDeathCountedEntities();

    Set<UUID> getDeathCountedEntityUUIDs();
}

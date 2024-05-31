package com.yummy.naraka.entity;

import net.minecraft.world.entity.LivingEntity;

import java.util.Set;

public interface DeathCountingEntity {
    Set<LivingEntity> getDeathCountedEntities();
    void onDeathCountZero(LivingEntity livingEntity);
}

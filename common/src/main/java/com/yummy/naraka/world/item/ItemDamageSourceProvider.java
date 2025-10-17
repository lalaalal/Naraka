package com.yummy.naraka.world.item;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface ItemDamageSourceProvider {
    DamageSource naraka$getDamageSource(LivingEntity livingEntity);
}

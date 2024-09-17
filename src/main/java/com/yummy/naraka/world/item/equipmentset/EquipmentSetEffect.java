package com.yummy.naraka.world.item.equipmentset;

import net.minecraft.world.entity.LivingEntity;

public interface EquipmentSetEffect {
    default void activate(LivingEntity livingEntity) {

    }

    default void deactivate(LivingEntity livingEntity) {

    }
}

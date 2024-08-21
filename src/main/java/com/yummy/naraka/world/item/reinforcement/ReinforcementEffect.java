package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ReinforcementEffect {
    boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement);

    default void onReinforcementIncreased(ItemStack itemStack, int previousReinforcement, int currentReinforcement) {

    }

    default void onEquipped(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    default void onUnequipped(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }
}

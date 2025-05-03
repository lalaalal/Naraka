package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public interface ReinforcementEffect {
    boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement);

    Set<EquipmentSlot> getAvailableSlots();

    default boolean showInTooltip(int reinforcement) {
        return true;
    }

    default void onReinforcementIncreased(ItemStack itemStack, int previousReinforcement, int currentReinforcement) {

    }

    default void onEquipped(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    default void onEquippedItemChanged(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    default void onUnequipped(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }
}

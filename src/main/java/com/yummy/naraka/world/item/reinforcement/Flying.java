package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class Flying implements ReinforcementEffect {
    @Override
    public boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement) {
        return equipmentSlot == EquipmentSlot.BODY && reinforcement >= 10;
    }

    @Override
    public void onEquipped(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {
        entity.setNoGravity(true);
    }

    @Override
    public void onUnequipped(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {
        entity.setNoGravity(false);
    }
}

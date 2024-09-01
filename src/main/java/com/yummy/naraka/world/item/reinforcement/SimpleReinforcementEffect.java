package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SimpleReinforcementEffect implements ReinforcementEffect {
    private final EquipmentSlotGroup slots;
    private final int requiredReinforcement;

    public SimpleReinforcementEffect(EquipmentSlotGroup slots, int requiredReinforcement) {
        this.slots = slots;
        this.requiredReinforcement = requiredReinforcement;
    }

    public SimpleReinforcementEffect(EquipmentSlot slot, int requiredReinforcement) {
        this.slots = EquipmentSlotGroup.bySlot(slot);
        this.requiredReinforcement = requiredReinforcement;
    }

    @Override
    public boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement) {
        return slots.test(equipmentSlot) && reinforcement >= requiredReinforcement;
    }
    
    @Override
    public boolean showInTooltip(int reinforcement) {
        return reinforcement >= requiredReinforcement;
    }
}

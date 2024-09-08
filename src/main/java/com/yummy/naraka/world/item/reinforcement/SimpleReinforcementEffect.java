package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class SimpleReinforcementEffect implements ReinforcementEffect {
    private final int requiredReinforcement;
    private final Set<EquipmentSlot> slots;

    public SimpleReinforcementEffect(int requiredReinforcement, EquipmentSlot... slots) {
        this.requiredReinforcement = requiredReinforcement;
        this.slots = Set.of(slots);
    }

    @Override
    public Set<EquipmentSlot> getAvailableSlots() {
        return slots;
    }

    @Override
    public boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement) {
        return slots.contains(equipmentSlot) && reinforcement >= requiredReinforcement;
    }

    @Override
    public boolean showInTooltip(int reinforcement) {
        return reinforcement >= requiredReinforcement;
    }
}

package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class KnockbackResistance extends AttributeModifyingEffect {
    public KnockbackResistance(EquipmentSlot... slots) {
        super(Attributes.KNOCKBACK_RESISTANCE, slots);
    }

    @Override
    public boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement) {
        return getAvailableSlots().contains(equipmentSlot) && reinforcement >= 10;
    }

    @Override
    public boolean showInTooltip(int reinforcement) {
        return reinforcement >= 10;
    }

    @Override
    protected AttributeModifier createModifier(int reinforcement) {
        return new AttributeModifier(
                modifierId("knockback_resistance"),
                1,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    @Override
    protected EquipmentSlotGroup getTargetSlot(ItemStack itemStack) {
        return EquipmentSlotGroup.FEET;
    }

    @Override
    public void onReinforcementIncreased(ItemStack itemStack, int previousReinforcement, int currentReinforcement) {
        if (currentReinforcement >= 10)
            super.onReinforcementIncreased(itemStack, previousReinforcement, currentReinforcement);
    }
}

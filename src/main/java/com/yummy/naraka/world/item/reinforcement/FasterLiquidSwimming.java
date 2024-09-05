package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class FasterLiquidSwimming extends AttributeModifyingEffect {
    protected FasterLiquidSwimming() {
        super(Attributes.WATER_MOVEMENT_EFFICIENCY);
    }

    @Override
    protected AttributeModifier createModifier(int reinforcement) {
        return new AttributeModifier(
                modifierId("water_movement_efficiency"),
                5,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    @Override
    protected EquipmentSlotGroup getTargetSlot(ItemStack itemStack) {
        return EquipmentSlotGroup.LEGS;
    }

    @Override
    public boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement) {
        return equipmentSlot == EquipmentSlot.LEGS && reinforcement >= 10;
    }
}

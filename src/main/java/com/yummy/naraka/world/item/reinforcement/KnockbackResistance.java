package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class KnockbackResistance extends AttributeModifingEffect {
    public KnockbackResistance() {
        super(Attributes.KNOCKBACK_RESISTANCE);
    }

    @Override
    public boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement) {
        return equipmentSlot == EquipmentSlot.FEET && reinforcement >= 10;
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
}

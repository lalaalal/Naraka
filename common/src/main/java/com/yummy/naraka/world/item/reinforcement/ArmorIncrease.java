package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class ArmorIncrease extends AttributeModifyingEffect {
    private static final int[] DEFENCE_POINTS = {
            1, 1, 2, 2, 2, 3, 3, 6, 12, 50
    };

    public ArmorIncrease() {
        super(Attributes.ARMOR, EquipmentSlotGroup.ARMOR);
    }

    @Override
    public boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement) {
        return itemStack.is(ItemTags.ARMOR_ENCHANTABLE);
    }

    @Override
    protected AttributeModifier createModifier(int reinforcement) {
        reinforcement = Mth.clamp(reinforcement, 0, DEFENCE_POINTS.length);
        int defence = 0;
        for (int i = 0; i < reinforcement; i++)
            defence += DEFENCE_POINTS[i];
        return new AttributeModifier(modifierId("armor"), defence, AttributeModifier.Operation.ADD_VALUE);
    }
}

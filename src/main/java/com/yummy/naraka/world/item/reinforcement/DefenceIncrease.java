package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import java.util.Map;
import java.util.function.Predicate;

public class DefenceIncrease extends AttributeModifingEffect {
    private static final int[] DEFENCE_POINTS = {
            1, 1, 2, 2, 2, 3, 3, 6, 12, 50
    };

    private static final Map<Predicate<ItemStack>, EquipmentSlotGroup> SLOT_GROUP_MAP = Map.of(
            itemStack -> itemStack.is(ItemTags.HEAD_ARMOR), EquipmentSlotGroup.HEAD,
            itemStack -> itemStack.is(ItemTags.CHEST_ARMOR), EquipmentSlotGroup.CHEST,
            itemStack -> itemStack.is(ItemTags.LEG_ARMOR), EquipmentSlotGroup.LEGS,
            itemStack -> itemStack.is(ItemTags.FOOT_ARMOR), EquipmentSlotGroup.FEET
    );

    public DefenceIncrease() {
        super(Attributes.ARMOR);
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

    @Override
    protected EquipmentSlotGroup getTargetSlot(ItemStack itemStack) {
        for (Predicate<ItemStack> predicate : SLOT_GROUP_MAP.keySet()) {
            if (predicate.test(itemStack))
                return SLOT_GROUP_MAP.get(predicate);
        }
        return EquipmentSlotGroup.ARMOR;
    }
}

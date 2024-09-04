package com.yummy.naraka.util;

import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class NarakaItemUtils {
    @SuppressWarnings("deprecation")
    public static ItemAttributeModifiers getAttributeModifiers(ItemStack itemStack) {
        ItemAttributeModifiers modifiers = itemStack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers == null || modifiers.modifiers().isEmpty())
            return itemStack.getItem().getDefaultAttributeModifiers();
        return modifiers;
    }

    public static boolean canApplyReinforcementEffect(LivingEntity livingEntity, EquipmentSlot slot, Holder<ReinforcementEffect> effect) {
        ItemStack itemStack = livingEntity.getItemBySlot(slot);
        Reinforcement reinforcement = itemStack.getOrDefault(NarakaDataComponentTypes.REINFORCEMENT, Reinforcement.ZERO);
        return reinforcement.canApplyEffect(effect, livingEntity, slot, itemStack);
    }

    public static void updateReinforcementEffects(LivingEntity livingEntity) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = livingEntity.getItemBySlot(slot);
            Reinforcement reinforcement = itemStack.getOrDefault(NarakaDataComponentTypes.REINFORCEMENT, Reinforcement.ZERO);
            for (Holder<ReinforcementEffect> effect : reinforcement.effects()) {
                if (effect.value().canApply(livingEntity, slot, itemStack, reinforcement.value()))
                    effect.value().onEquipped(livingEntity, slot, itemStack);
            }
        }
    }
}

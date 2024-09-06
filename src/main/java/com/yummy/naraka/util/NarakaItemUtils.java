package com.yummy.naraka.util;

import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class NarakaItemUtils {
    /**
     * @return Get item attribute modifiers, item's default modifier if null or empty
     */
    @SuppressWarnings("deprecation")
    public static ItemAttributeModifiers getAttributeModifiers(ItemStack itemStack) {
        ItemAttributeModifiers modifiers = itemStack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers == null || modifiers.modifiers().isEmpty())
            return itemStack.getItem().getDefaultAttributeModifiers();
        return modifiers;
    }

    public static boolean canApplyReinforcementEffect(LivingEntity livingEntity, Holder<ReinforcementEffect> effect) {
        for (EquipmentSlot slot : effect.value().getAvailableSlots()) {
            ItemStack itemStack = livingEntity.getItemBySlot(slot);
            Reinforcement reinforcement = Reinforcement.get(itemStack);
            if (reinforcement.canApplyEffect(effect, livingEntity, slot, itemStack))
                return true;
        }
        return false;
    }

    public static void updateReinforcementEffects(LivingEntity livingEntity) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = livingEntity.getItemBySlot(slot);
            Reinforcement reinforcement = Reinforcement.get(itemStack);
            for (Holder<ReinforcementEffect> effect : reinforcement.effects()) {
                if (effect.value().canApply(livingEntity, slot, itemStack, reinforcement.value()))
                    effect.value().onEquipped(livingEntity, slot, itemStack);
            }
        }
    }

    public static boolean canApplyFlying(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.FLYING);
    }

    public static boolean canApplyOreSeeThrough(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.ORE_SEE_THROUGH);
    }
    
    public static boolean canApplyFasterLiquidSwimming(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.FASTER_LIQUID_SWIMMING);
    }
}

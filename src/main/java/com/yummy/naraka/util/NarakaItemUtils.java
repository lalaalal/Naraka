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

    public static boolean canApplyFlying(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.FLYING);
    }

    public static boolean canApplyOreSeeThrough(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.ORE_SEE_THROUGH);
    }

    public static boolean canApplyFasterLiquidSwimming(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.FASTER_LIQUID_SWIMMING);
    }

    public static boolean canApplyIgnoreLiquidPushing(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.IGNORE_LIQUID_PUSHING);
    }

    public static boolean canApplyLavaVision(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.LAVA_VISION);
    }

    public static boolean canApplyEfficientMiningInAir(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.EFFICIENT_MINING_IN_AIR);
    }

    public static boolean canApplyEfficientMiningInWater(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.EFFICIENT_MINING_IN_WATER);
    }

    public static boolean canApplyWaterBreathing(LivingEntity livingEntity) {
        return canApplyReinforcementEffect(livingEntity, NarakaReinforcementEffects.WATER_BREATHING);
    }

    public static void checkAndUpdateReinforcementEffects(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack itemStack, EquippedItemChangeListener listener) {
        Reinforcement reinforcement = Reinforcement.get(itemStack);
        for (Holder<ReinforcementEffect> holder : reinforcement.effects()) {
            ReinforcementEffect effect = holder.value();
            if (effect.canApply(livingEntity, equipmentSlot, itemStack, reinforcement.value()))
                listener.onChange(effect, livingEntity, equipmentSlot, itemStack);
        }
    }

    public static void updateReinforcementEffects(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack itemStack, EquippedItemChangeListener listener) {
        Reinforcement reinforcement = Reinforcement.get(itemStack);
        for (Holder<ReinforcementEffect> holder : reinforcement.effects()) {
            ReinforcementEffect effect = holder.value();
            listener.onChange(effect, livingEntity, equipmentSlot, itemStack);
        }
    }

    public static void updateAllReinforcementEffects(LivingEntity livingEntity) {
        for (ItemStack itemStack : livingEntity.getAllSlots()) {
            EquipmentSlot slot = livingEntity.getEquipmentSlotForItem(itemStack);
            checkAndUpdateReinforcementEffects(livingEntity, slot, itemStack, ReinforcementEffect::onEquipped);
        }
    }

    @FunctionalInterface
    public interface EquippedItemChangeListener {
        void onChange(ReinforcementEffect effect, LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack itemStack);
    }
}

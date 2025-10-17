package com.yummy.naraka.util;

import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NarakaItemUtils {
    /**
     * @return Get item attribute modifiers, item's default modifier if null or empty
     */
    public static ItemAttributeModifiers getAttributeModifiers(ItemStack itemStack) {
        ItemAttributeModifiers modifiers = itemStack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        DataComponentMap components = itemStack.getItem().components();
        if (modifiers == null || modifiers.modifiers().isEmpty())
            return getAttributeModifiers(components);
        return modifiers;
    }

    private static ItemAttributeModifiers getAttributeModifiers(DataComponentMap components) {
        ItemAttributeModifiers modifiers = components.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers == null)
            return ItemAttributeModifiers.EMPTY;
        return modifiers;
    }

    public static void summonItemEntity(Level level, ItemStack itemStack, BlockPos pos) {
        if (!level.isClientSide()) {
            level.addFreshEntity(new ItemEntity(
                    level,
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5,
                    itemStack
            ));
        }
    }

    public static void saveBlockEntity(ItemStack itemStack, BlockEntity blockEntity, HolderLookup.Provider provider) {
        itemStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntity.saveWithFullMetadata(provider)));
    }

    public static void loadBlockEntity(ItemStack itemStack, BlockEntity blockEntity, HolderLookup.Provider provider) {
        CustomData customData = itemStack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(new CompoundTag()));
        customData.loadInto(blockEntity, provider);
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
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = livingEntity.getItemBySlot(slot);
            checkAndUpdateReinforcementEffects(livingEntity, slot, itemStack, ReinforcementEffect::onEquipped);
        }
    }

    @FunctionalInterface
    public interface EquippedItemChangeListener {
        void onChange(ReinforcementEffect effect, LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack itemStack);
    }
}

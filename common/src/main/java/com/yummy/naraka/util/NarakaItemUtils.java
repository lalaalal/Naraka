package com.yummy.naraka.util;

import com.mojang.serialization.Codec;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class NarakaItemUtils {
    public static final String TAG_UNBREAKABLE = "Unbreakable";
    public static final String TAG_BLOCK_DATA = "BlockData";
    public static final String TAG_BLESSED = "Blessed";
    public static final String TAG_HEROBRINE_SCARF = "HerobrineScarf";
    public static final String TAG_SOUL_TYPE = "SoulType";
    public static final String TAG_EQUIPMENT_SET = "EquipmentSet";
    public static final String TAG_ITEM_DETAIL = "ItemDetail";

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

    public static ItemStack makeUnbreakable(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putBoolean(NarakaItemUtils.TAG_UNBREAKABLE, true);

        return itemStack;
    }

    public static <T> T readNbtDataOrDefault(ItemStack itemStack, String key, Codec<T> codec, T defaultValue) {
        CompoundTag tag = itemStack.getOrCreateTag();
        return NarakaNbtUtils.readOr(tag, key, codec, defaultValue);
    }

    public static <T> T readNbtDataOrDefault(ItemStack itemStack, String key, Codec<T> codec, HolderLookup.Provider registries, T defaultValue) {
        CompoundTag tag = itemStack.getOrCreateTag();
        return NarakaNbtUtils.readOr(tag, key, codec, RegistryOps.create(NbtOps.INSTANCE, registries), defaultValue);
    }

    public static <T> ItemStack storeNbtData(ItemStack itemStack, String key, Codec<T> codec, T value) {
        CompoundTag tag = itemStack.getOrCreateTag();
        NarakaNbtUtils.store(tag, key, codec, value);
        return itemStack;
    }

    public static <T> ItemStack storeNbtData(ItemStack itemStack, String key, Codec<T> codec, HolderLookup.Provider registries, T value) {
        CompoundTag tag = itemStack.getOrCreateTag();
        NarakaNbtUtils.store(tag, key, codec, RegistryOps.create(NbtOps.INSTANCE, registries), value);
        return itemStack;
    }

    public static boolean hasNbtData(ItemStack itemStack, String key) {
        CompoundTag tag = itemStack.getTag();
        if (tag == null)
            return false;
        return tag.contains(key);
    }

    public static void saveBlockEntity(ItemStack itemStack, BlockEntity blockEntity) {
        blockEntity.saveToItem(itemStack);
    }

    public static void loadBlockEntity(ItemStack itemStack, BlockEntity blockEntity) {
        CompoundTag result = BlockItem.getBlockEntityData(itemStack);
        if (result != null)
            blockEntity.load(result);
    }

    public static void loadBlockEntity(ItemStack itemStack, BlockEntity blockEntity, CompoundTag defaultValue) {
        CompoundTag result = BlockItem.getBlockEntityData(itemStack);
        blockEntity.load(Objects.requireNonNullElse(result, defaultValue));
    }

    public static boolean canApplyReinforcementEffect(LivingEntity livingEntity, Holder<ReinforcementEffect> effect) {
        for (EquipmentSlot slot : effect.value().getAvailableSlots()) {
            ItemStack itemStack = livingEntity.getItemBySlot(slot);
            Reinforcement reinforcement = Reinforcement.get(itemStack, livingEntity.level().registryAccess());
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
        Reinforcement reinforcement = Reinforcement.get(itemStack, livingEntity.level().registryAccess());
        for (Holder<ReinforcementEffect> holder : reinforcement.effects()) {
            ReinforcementEffect effect = holder.value();
            if (effect.canApply(livingEntity, equipmentSlot, itemStack, reinforcement.value()))
                listener.onChange(effect, livingEntity, equipmentSlot, itemStack);
        }
    }

    public static void updateReinforcementEffects(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack itemStack, EquippedItemChangeListener listener) {
        Reinforcement reinforcement = Reinforcement.get(itemStack, livingEntity.level().registryAccess());
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

package com.yummy.naraka.world.item.reinforcement;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.RegistryInitializer;
import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Predicate;

public class NarakaReinforcementEffects {
    public static final Holder<ReinforcementEffect> INCREASE_ATTACK_DAMAGE = register(
            "increase_attack_damage", AttributeModifyingEffect.simple(Attributes.ATTACK_DAMAGE, EquipmentSlotGroup.MAINHAND)
    );

    public static final Holder<ReinforcementEffect> INCREASE_ARMOR = register(
            "increase_armor", new ArmorIncrease()
    );

    public static final Holder<ReinforcementEffect> INCREASE_ARMOR_TOUGHNESS = register(
            "increase_armor_toughness", AttributeModifyingEffect.simple(Attributes.ARMOR_TOUGHNESS, EquipmentSlotGroup.ARMOR)
    );

    public static final Holder<ReinforcementEffect> KNOCKBACK_RESISTANCE = register(
            "increase_knockback_resistance", new KnockbackResistance(EquipmentSlotGroup.ARMOR)
    );

    public static final Holder<ReinforcementEffect> FASTER_LIQUID_SWIMMING = register(
            "faster_liquid_swimming", new SimpleReinforcementEffect(10, EquipmentSlot.FEET)
    );

    public static final Holder<ReinforcementEffect> IGNORE_LIQUID_PUSHING = register(
            "ignore_liquid_pushing", new SimpleReinforcementEffect(10, EquipmentSlot.FEET)
    );

    public static final Holder<ReinforcementEffect> FLYING = register(
            "flying", new Flying(EquipmentSlot.CHEST)
    );

    public static final Holder<ReinforcementEffect> ORE_SEE_THROUGH = register(
            "ore_see_through", new SimpleReinforcementEffect(10, EquipmentSlot.HEAD)
    );

    public static final Holder<ReinforcementEffect> LAVA_VISION = register(
            "lava_vision", new SimpleReinforcementEffect(10, EquipmentSlot.HEAD)
    );

    public static final Holder<ReinforcementEffect> EFFICIENT_MINING_IN_AIR = register(
            "efficient_mining_in_air", new SimpleReinforcementEffect(10, EquipmentSlot.FEET)
    );

    public static final Holder<ReinforcementEffect> EFFICIENT_MINING_IN_WATER = register(
            "efficient_mining_in_water", new SimpleReinforcementEffect(10, EquipmentSlot.HEAD)
    );

    public static final Holder<ReinforcementEffect> WATER_BREATHING = register(
            "water_breathing", new SimpleReinforcementEffect(10, EquipmentSlot.HEAD)
    );

    private static Holder<ReinforcementEffect> register(String name, ReinforcementEffect effect) {
        return RegistryProxy.register(NarakaRegistries.Keys.REINFORCEMENT_EFFECT, name, () -> effect);
    }

    private static final Map<Predicate<ItemStack>, List<Holder<ReinforcementEffect>>> ITEM_REINFORCEMENT_EFFECTS = new LinkedHashMap<>();

    @SafeVarargs
    public static void addEffectsByItem(Item item, Holder<ReinforcementEffect>... effects) {
        addEffectsByItem(test -> test.is(item), effects);
    }

    @SafeVarargs
    public static void addEffectsByItem(TagKey<Item> itemTag, Holder<ReinforcementEffect>... effects) {
        addEffectsByItem(test -> test.is(itemTag), effects);
    }

    @SafeVarargs
    public static void addEffectsByItem(Predicate<ItemStack> predicate, Holder<ReinforcementEffect>... effects) {
        ITEM_REINFORCEMENT_EFFECTS.put(predicate, List.of(effects));
    }

    @SafeVarargs
    public static void addEffectsByItem(TagPredicate<Item> predicate, Holder<ReinforcementEffect>... effects) {
        ITEM_REINFORCEMENT_EFFECTS.put(itemStack -> predicate.matches(itemStack.getItemHolder()), List.of(effects));
    }

    public static HolderSet<ReinforcementEffect> byItem(ItemStack itemStack) {
        Set<Holder<ReinforcementEffect>> effects = new LinkedHashSet<>();
        for (Predicate<ItemStack> predicate : ITEM_REINFORCEMENT_EFFECTS.keySet()) {
            if (predicate.test(itemStack))
                effects.addAll(ITEM_REINFORCEMENT_EFFECTS.get(predicate));
        }
        return HolderSet.direct(List.copyOf(effects));
    }

    public static void initialize() {
        RegistryInitializer.get(NarakaRegistries.Keys.REINFORCEMENT_EFFECT)
                .onRegistrationFinished();

        addEffectsByItem(ItemTags.SWORDS, INCREASE_ATTACK_DAMAGE);
        addEffectsByItem(ItemTags.TRIDENT_ENCHANTABLE, INCREASE_ATTACK_DAMAGE);
        addEffectsByItem(NarakaItemTags.SPEAR_ENCHANTABLE, INCREASE_ATTACK_DAMAGE);
        addEffectsByItem(ItemTags.ARMOR_ENCHANTABLE, INCREASE_ARMOR, INCREASE_ARMOR_TOUGHNESS);
        addEffectsByItem(and(isBlessed(), is(ItemTags.HEAD_ARMOR_ENCHANTABLE)), ORE_SEE_THROUGH, LAVA_VISION, EFFICIENT_MINING_IN_WATER, WATER_BREATHING);
        addEffectsByItem(and(isBlessed(), is(ItemTags.CHEST_ARMOR_ENCHANTABLE)), FLYING);
        addEffectsByItem(ItemTags.LEG_ARMOR_ENCHANTABLE, KNOCKBACK_RESISTANCE);
        addEffectsByItem(and(isBlessed(), is(ItemTags.FOOT_ARMOR_ENCHANTABLE)), FASTER_LIQUID_SWIMMING, IGNORE_LIQUID_PUSHING, EFFICIENT_MINING_IN_AIR);
    }

    private static Predicate<ItemStack> isBlessed() {
        return itemStack -> itemStack.getOrDefault(NarakaDataComponentTypes.BLESSED.get(), false);
    }

    private static Predicate<ItemStack> and(Predicate<ItemStack> left, Predicate<ItemStack> right) {
        return itemStack -> left.test(itemStack) && right.test(itemStack);
    }

    private static Predicate<ItemStack> is(TagKey<Item> tag) {
        return itemStack -> itemStack.is(tag);
    }
}
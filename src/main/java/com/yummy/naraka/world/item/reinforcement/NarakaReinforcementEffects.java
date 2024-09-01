package com.yummy.naraka.world.item.reinforcement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Predicate;

public class NarakaReinforcementEffects {
    public static final Holder<ReinforcementEffect> DAMAGE_INCREASE = register(
            "damage_increase", new DamageIncrease()
    );

    public static final Holder<ReinforcementEffect> DEFENCE_INCREASE = register(
            "defence_increase", new DefenceIncrease()
    );

    public static final Holder<ReinforcementEffect> KNOCKBACK_RESISTANCE = register(
            "knockback_resistance", new KnockbackResistance()
    );

    public static final Holder<ReinforcementEffect> IGNORE_WATER_PUSH = register(
            "ignore_water_push",
            (entity, equipmentSlot, itemStack, reinforcement) -> equipmentSlot == EquipmentSlot.LEGS && reinforcement == 10
    );

    private static Holder<ReinforcementEffect> register(String name, ReinforcementEffect effect) {
        return Registry.registerForHolder(NarakaRegistries.REINFORCEMENT_EFFECT, NarakaMod.location(name), effect);
    }

    private static final Map<Predicate<ItemStack>, List<Holder<ReinforcementEffect>>> ITEM_REINFORCEMENT_EFFECTS = new HashMap<>();

    @SafeVarargs
    public static void add(Item item, Holder<ReinforcementEffect>... effects) {
        add(test -> test.is(item), effects);
    }

    @SafeVarargs
    public static void add(TagKey<Item> itemTag, Holder<ReinforcementEffect>... effects) {
        add(test -> test.is(itemTag), effects);
    }

    @SafeVarargs
    public static void add(Predicate<ItemStack> predicate, Holder<ReinforcementEffect>... effects) {
        ITEM_REINFORCEMENT_EFFECTS.put(predicate, List.of(effects));
    }

    public static HolderSet<ReinforcementEffect> get(ItemStack itemStack) {
        Set<Holder<ReinforcementEffect>> effects = new HashSet<>();
        for (Predicate<ItemStack> predicate : ITEM_REINFORCEMENT_EFFECTS.keySet()) {
            if (predicate.test(itemStack))
                effects.addAll(ITEM_REINFORCEMENT_EFFECTS.get(predicate));
        }
        return HolderSet.direct(List.copyOf(effects));
    }

    public static void initialize() {
        add(ItemTags.SWORDS, DAMAGE_INCREASE);
        add(ItemTags.TRIDENT_ENCHANTABLE, DAMAGE_INCREASE);
        add(ItemTags.ARMOR_ENCHANTABLE, DEFENCE_INCREASE);
        add(ItemTags.LEG_ARMOR_ENCHANTABLE, IGNORE_WATER_PUSH);
        add(ItemTags.FOOT_ARMOR_ENCHANTABLE, KNOCKBACK_RESISTANCE);
    }
}

package com.yummy.naraka.item.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class NarakaEnchantments {
    private static Registry<Enchantment> registry;

    public static void initialize(RegistryAccess registryAccess) {
        registry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> enchantmentHolderGetter = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> itemHolderGetter = context.lookup(Registries.ITEM);
    }

    public static Holder<Enchantment> get(ResourceKey<Enchantment> key) {
        return registry.getHolderOrThrow(key);
    }
}

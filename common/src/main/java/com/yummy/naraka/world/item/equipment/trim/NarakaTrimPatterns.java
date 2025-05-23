package com.yummy.naraka.world.item.equipment.trim;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.item.equipment.trim.TrimPatterns;

import java.util.Optional;

public class NarakaTrimPatterns {
    private static final String TEMPLATE_ITEM_SUFFIX = "_armor_trim_smithing_template";

    public static final ResourceKey<TrimPattern> PURIFIED_SOUL_SILENCE = create("purified_soul_silence");

    public static void bootstrap(BootstrapContext<TrimPattern> context) {
        TrimPatterns.register(context, PURIFIED_SOUL_SILENCE);
    }

    private static ResourceKey<TrimPattern> fromItem(Item template) {
        ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(template);
        String path = itemLocation.getPath();
        String trimPatternKeyPath = path.replaceFirst(TEMPLATE_ITEM_SUFFIX, "");
        ResourceLocation trimPatternLocation = ResourceLocation.fromNamespaceAndPath(itemLocation.getNamespace(), trimPatternKeyPath);
        return ResourceKey.create(Registries.TRIM_PATTERN, trimPatternLocation);
    }

    public static Optional<Holder.Reference<TrimPattern>> fromItem(RegistryAccess registry, ItemStack template) {
        ResourceKey<TrimPattern> key = fromItem(template.getItem());
        HolderGetter<TrimPattern> trimPatterns = registry.lookupOrThrow(Registries.TRIM_PATTERN);
        return trimPatterns.get(key);
    }

    private static ResourceKey<TrimPattern> create(String name) {
        return ResourceKey.create(Registries.TRIM_PATTERN, NarakaMod.location(name));
    }
}

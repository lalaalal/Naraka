package com.yummy.naraka.init;

import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.Consumer;

public interface NarakaInitializer {
    RegistryFactory getRegistryFactory();

    void modifyCreativeModeTab(ResourceKey<CreativeModeTab> tabKey, Consumer<NarakaCreativeModTabs.TabEntries> entries);

    void registerFeatureBiomeModifier(String name, TagKey<Biome> target, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> featureKeys);
}

package com.yummy.naraka.fabric.init;

import com.yummy.naraka.init.BiomeModificationRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.Supplier;

public final class FabricBiomeModificationRegistry {
    @MethodProxy(BiomeModificationRegistry.class)
    public static void addFeatures(String name, TagKey<Biome> biomes, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> features) {
        for (ResourceKey<PlacedFeature> feature : features)
            BiomeModifications.addFeature(BiomeSelectors.tag(biomes), step, feature);
    }

    @MethodProxy(BiomeModificationRegistry.class)
    public static <T extends Mob> void addSpawns(String name, TagKey<Biome> target, MobCategory spawnGroup, Supplier<EntityType<T>> entityType, int weight, int minGroupSize, int maxGroupSize) {
        BiomeModifications.addSpawn(BiomeSelectors.tag(target), spawnGroup, entityType.get(), weight, minGroupSize, maxGroupSize);
    }
}

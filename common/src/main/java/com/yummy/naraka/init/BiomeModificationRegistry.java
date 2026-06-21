package com.yummy.naraka.init;

import com.yummy.naraka.service.NarakaServices;
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

public abstract class BiomeModificationRegistry {
    public static void addFeatures(String name, TagKey<Biome> biomes, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> features) {
        NarakaServices.BIOME_MODIFIER.addFeatures(name, biomes, step, features);
    }

    public static <T extends Mob> void addSpawns(String name, TagKey<Biome> target, MobCategory spawnGroup, Supplier<EntityType<T>> entityType, int weight, int minGroupSize, int maxGroupSize) {
        NarakaServices.BIOME_MODIFIER.addSpawns(name, target, spawnGroup, entityType, weight, minGroupSize, maxGroupSize);
    }

    public interface Registrar {
        void addFeatures(String name, TagKey<Biome> biomes, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> features);

        <T extends Mob> void addSpawns(String name, TagKey<Biome> target, MobCategory spawnGroup, Supplier<EntityType<T>> entityType, int weight, int minGroupSize, int maxGroupSize);
    }
}

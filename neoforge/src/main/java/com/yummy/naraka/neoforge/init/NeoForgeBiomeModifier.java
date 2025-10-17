package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NeoForgeBiomeModifier implements NarakaBiomes.Modifier {
    public static final NeoForgeBiomeModifier INSTANCE = new NeoForgeBiomeModifier();

    private final List<BiomeModifierRecord> modifiers = new ArrayList<>();

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        for (BiomeModifierRecord modifier : INSTANCE.modifiers)
            modifier.register(context);
    }

    private NeoForgeBiomeModifier() {
    }

    @Override
    public void addFeatures(String name, TagKey<Biome> biomes, GenerationStep.Decoration generationStep, List<ResourceKey<PlacedFeature>> features) {
        modifiers.add(context -> {
            HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
            HolderGetter<PlacedFeature> featureGetter = context.lookup(Registries.PLACED_FEATURE);

            HolderSet<Biome> targetBiomes = biomeGetter.getOrThrow(biomes);

            context.register(create(name),
                    new BiomeModifiers.AddFeaturesBiomeModifier(
                            targetBiomes,
                            HolderSet.direct(featureGetter::getOrThrow, features),
                            generationStep
                    )
            );
        });
    }

    @Override
    public <T extends Mob> void addSpawns(String name, TagKey<Biome> biomes, MobCategory spawnGroup, Supplier<EntityType<T>> entityType, int weight, int minGroupSize, int maxGroupSize) {
        modifiers.add(context -> {
            HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
            HolderSet<Biome> targetBiomes = biomeGetter.getOrThrow(biomes);

            List<MobSpawnSettings.SpawnerData> spawners = new ArrayList<>();
            spawners.add(new MobSpawnSettings.SpawnerData(entityType.get(), weight, minGroupSize, maxGroupSize));
            context.register(create(name), new BiomeModifiers.AddSpawnsBiomeModifier(targetBiomes, spawners));
        });

    }

    private static ResourceKey<BiomeModifier> create(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NarakaMod.location(name));
    }

    @FunctionalInterface
    private interface BiomeModifierRecord {
        void register(BootstrapContext<BiomeModifier> context);
    }
}

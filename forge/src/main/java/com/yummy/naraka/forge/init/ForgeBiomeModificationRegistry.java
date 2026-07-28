package com.yummy.naraka.forge.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.init.BiomeModificationRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ForgeBiomeModificationRegistry implements BiomeModificationRegistry.Registrar {
    private static final List<BiomeModifierRecord> modifiers = new ArrayList<>();

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        for (BiomeModifierRecord modifier : modifiers)
            modifier.register(context);
    }

    @Override
    public void addFeatures(String name, TagKey<Biome> biomes, GenerationStep.Decoration generationStep, List<ResourceKey<PlacedFeature>> features) {
        modifiers.add(context -> {
            HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
            HolderGetter<PlacedFeature> featureGetter = context.lookup(Registries.PLACED_FEATURE);

            HolderSet<Biome> targetBiomes = biomeGetter.getOrThrow(biomes);

            context.register(create(name),
                    new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
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
            context.register(create(name), new ForgeBiomeModifiers.AddSpawnsBiomeModifier(targetBiomes, spawners));
        });
    }

    private static ResourceKey<BiomeModifier> create(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, NarakaMod.location(name));
    }

    @FunctionalInterface
    private interface BiomeModifierRecord {
        void register(BootstapContext<BiomeModifier> context);
    }
}

package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.init.BiomeModificationRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedList;
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

public final class NeoForgeBiomeModificationRegistry {
    private static final List<BiomeModifierRecord> modifiers = new ArrayList<>();

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        for (BiomeModifierRecord modifier : modifiers)
            modifier.register(context);
    }

    @MethodProxy(BiomeModificationRegistry.class)
    public static void addFeatures(String name, TagKey<Biome> biomes, GenerationStep.Decoration generationStep, List<ResourceKey<PlacedFeature>> features) {
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

    @MethodProxy(BiomeModificationRegistry.class)
    public static <T extends Mob> void addSpawns(String name, TagKey<Biome> biomes, MobCategory spawnGroup, Supplier<EntityType<T>> entityType, int weight, int minGroupSize, int maxGroupSize) {
        modifiers.add(context -> {
            HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
            HolderSet<Biome> targetBiomes = biomeGetter.getOrThrow(biomes);

            WeightedList<MobSpawnSettings.SpawnerData> spawners = WeightedList.<MobSpawnSettings.SpawnerData>builder()
                    .add(new MobSpawnSettings.SpawnerData(entityType.get(), minGroupSize, maxGroupSize), weight)
                    .build();
            context.register(create(name), new BiomeModifiers.AddSpawnsBiomeModifier(targetBiomes, spawners));
        });
    }

    private static ResourceKey<BiomeModifier> create(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NarakaMod.identifier(name));
    }

    @FunctionalInterface
    private interface BiomeModifierRecord {
        void register(BootstrapContext<BiomeModifier> context);
    }
}

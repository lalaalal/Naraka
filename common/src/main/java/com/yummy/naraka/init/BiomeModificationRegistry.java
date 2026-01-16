package com.yummy.naraka.init;

import com.yummy.naraka.invoker.MethodInvoker;
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
        MethodInvoker.of(BiomeModificationRegistry.class, "addFeatures")
                .withParameterTypes(String.class, TagKey.class, GenerationStep.Decoration.class, List.class)
                .invoke(name, biomes, step, features);
    }

    public static <T extends Mob> void addSpawns(String name, TagKey<Biome> target, MobCategory spawnGroup, Supplier<EntityType<T>> entityType, int weight, int minGroupSize, int maxGroupSize) {
        MethodInvoker.of(BiomeModificationRegistry.class, "addSpawns")
                .withParameterTypes(String.class, TagKey.class, MobCategory.class, Supplier.class, int.class, int.class, int.class)
                .invoke(name, target, spawnGroup, entityType, weight, minGroupSize, maxGroupSize);
    }
}

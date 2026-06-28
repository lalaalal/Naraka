package com.yummy.naraka.fabric.data;

import com.yummy.naraka.data.worldgen.*;
import com.yummy.naraka.data.worldgen.features.NarakaConfiguredFeatures;
import com.yummy.naraka.data.worldgen.placement.NarakaPlacements;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimPatterns;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.RegistriesDatapackGenerator;

import java.util.concurrent.CompletableFuture;

public class NarakaDatapackProvider extends RegistriesDatapackGenerator {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, NarakaDamageTypes::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, NarakaConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, NarakaPlacements::bootstrap)
            .add(Registries.STRUCTURE, NarakaStructures::bootstrap)
            .add(Registries.STRUCTURE_SET, NarakaStructureSets::bootstrap)
            .add(Registries.TRIM_PATTERN, NarakaTrimPatterns::bootstrap)
            .add(Registries.TRIM_MATERIAL, NarakaTrimMaterials::bootstrap)
            .add(Registries.CONFIGURED_CARVER, NarakaConfiguredWorldCarvers::bootstrap)
            .add(Registries.BIOME, NarakaBiomeData::bootstrap)
            .add(Registries.DIMENSION_TYPE, NarakaDimensionTypes::bootstrap);

    private final CompletableFuture<HolderLookup.Provider> fullRegistries;

    public NarakaDatapackProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries.thenApply(provider -> BUILDER.build(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY))));
        fullRegistries = registries.thenApply(provider -> BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), provider));
    }

    public CompletableFuture<HolderLookup.Provider> getRegistryProvider() {
        return fullRegistries;
    }
}

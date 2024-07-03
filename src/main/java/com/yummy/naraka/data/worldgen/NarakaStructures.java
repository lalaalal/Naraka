package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.structure.HerobrineSanctuaryStructure;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.neoforged.neoforge.common.Tags;

import java.util.Map;

public class NarakaStructures {
    public static final ResourceKey<Structure> HEROBRINE_SANCTUARY = create("herobrine_sanctuary");

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        HolderSet<Biome> herobrineSanctuaryBiomes = biomes.getOrThrow(Tags.Biomes.IS_PLAINS);
        Map<MobCategory, StructureSpawnOverride> herobrineSanctuarySpawnOverrides = Map.of(
                MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST),
                MobCategory.CREATURE, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST)
        );
        context.register(
                HEROBRINE_SANCTUARY,
                new HerobrineSanctuaryStructure(
                        new Structure.StructureSettings.Builder(herobrineSanctuaryBiomes)
                                .spawnOverrides(herobrineSanctuarySpawnOverrides)
                                .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                                .build()
                )
        );
    }

    private static ResourceKey<Structure> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE, NarakaMod.location(name));
    }
}

package com.yummy.naraka.fabric.data.worldgen;

import com.yummy.naraka.world.structure.JumboPart;
import com.yummy.naraka.world.structure.JumboStructure;
import com.yummy.naraka.world.structure.height.SeaLevelBasedHeightProvider;
import com.yummy.naraka.world.structure.piece.NarakaStructurePieceFactories;
import com.yummy.naraka.world.structure.protection.NarakaProtectionPredicates;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NarakaStructures {
    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        HolderSet<Biome> herobrineSanctuaryBiomes = biomes.getOrThrow(ConventionalBiomeTags.IS_PLAINS);

        Map<MobCategory, StructureSpawnOverride> herobrineSanctuarySpawnOverrides = Map.of(
                MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST),
                MobCategory.CREATURE, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST)
        );

        context.register(
                com.yummy.naraka.world.structure.NarakaStructures.HEROBRINE_SANCTUARY,
                new JumboStructure(
                        new Structure.StructureSettings.Builder(herobrineSanctuaryBiomes)
                                .spawnOverrides(herobrineSanctuarySpawnOverrides)
                                .generationStep(GenerationStep.Decoration.VEGETAL_DECORATION)
                                .terrainAdapation(TerrainAdjustment.NONE)
                                .build(),
                        "herobrine_sanctuary",
                        Optional.of(NarakaProtectionPredicates.HEROBRINE_SANCTUARY_PROTECTION),
                        SeaLevelBasedHeightProvider.EXACT,
                        List.of(
                                new JumboPart("main", 3, 3, 4, com.yummy.naraka.world.structure.NarakaStructures.HEROBRINE_SANCTUARY_MAIN_OFFSET),
                                new JumboPart("bridge", 1, 1, 1, BlockPos.ZERO)
                        ),
                        List.of(
                                NarakaStructurePieceFactories.HEROBRINE_SANCTUARY_OUTLINE
                        ),
                        com.yummy.naraka.world.structure.NarakaStructures.HEROBRINE_SANCTUARY_OFFSET
                )
        );
    }
}

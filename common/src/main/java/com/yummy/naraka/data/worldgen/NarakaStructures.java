package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.world.NarakaBiomes;
import com.yummy.naraka.world.structure.JumboPart;
import com.yummy.naraka.world.structure.JumboStructure;
import com.yummy.naraka.world.structure.generation.NarakaStructureGenerationPointProviders;
import com.yummy.naraka.world.structure.piece.NarakaStructurePieceFactories;
import com.yummy.naraka.world.structure.protection.NarakaProtectionPredicates;
import net.minecraft.core.BlockPos;
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

import java.util.List;
import java.util.Map;

public class NarakaStructures {
    public static final ResourceKey<Structure> HEROBRINE_SANCTUARY = create("herobrine_sanctuary");
    public static final ResourceKey<Structure> NARAKA_PLATFORM = create("naraka_platform");

    public static final BlockPos HEROBRINE_SANCTUARY_OFFSET = new BlockPos(-8, -17, -48 * 2);
    public static final BlockPos HEROBRINE_SANCTUARY_MAIN_OFFSET = new BlockPos(-44, 0, 48);

    public static final BlockPos NARAKA_PLATFORM_OFFSET = new BlockPos(-25, 0, -25);

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        HolderSet<Biome> herobrineSanctuaryBiomes = biomes.getOrThrow(ConventionalTags.Biomes.IS_PLAINS);
        HolderSet<Biome> narakaPlatformBiomes = HolderSet.direct(biomes.getOrThrow(NarakaBiomes.HEROBRINE));

        Map<MobCategory, StructureSpawnOverride> herobrineSanctuarySpawnOverrides = Map.of(
                MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST),
                MobCategory.CREATURE, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST),
                MobCategory.AMBIENT, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST),
                MobCategory.MISC, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST)
        );

        context.register(
                HEROBRINE_SANCTUARY,
                new JumboStructure(
                        new Structure.StructureSettings.Builder(herobrineSanctuaryBiomes)
                                .spawnOverrides(herobrineSanctuarySpawnOverrides)
                                .generationStep(GenerationStep.Decoration.STRONGHOLDS)
                                .terrainAdapation(TerrainAdjustment.NONE)
                                .build(),
                        "herobrine_sanctuary",
                        NarakaProtectionPredicates.HEROBRINE_SANCTUARY_PROTECTION,
                        NarakaStructureGenerationPointProviders.HEROBRINE_SANCTUARY,
                        List.of(
                                new JumboPart("main", 3, 3, 3, 48, HEROBRINE_SANCTUARY_MAIN_OFFSET),
                                new JumboPart("bridge", 1, 1, 1, 48, BlockPos.ZERO)
                        ),
                        List.of(
                                NarakaStructurePieceFactories.HEROBRINE_SANCTUARY_OUTLINE
                        ),
                        HEROBRINE_SANCTUARY_OFFSET
                )
        );
        context.register(
                NARAKA_PLATFORM,
                new JumboStructure(
                        new Structure.StructureSettings.Builder(narakaPlatformBiomes)
                                .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                                .terrainAdapation(TerrainAdjustment.NONE)
                                .build(),
                        "naraka_platform",
                        NarakaProtectionPredicates.NOTHING,
                        NarakaStructureGenerationPointProviders.NARAKA_PLATFORM,
                        List.of(
                                new JumboPart("main", 2, 1, 2, 25, BlockPos.ZERO)
                        ),
                        List.of(),
                        NARAKA_PLATFORM_OFFSET
                )
        );
    }

    private static ResourceKey<Structure> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE, NarakaMod.identifier(name));
    }
}

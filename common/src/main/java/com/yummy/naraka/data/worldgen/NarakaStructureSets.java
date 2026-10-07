package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.NarakaBiomeTags;
import com.yummy.naraka.world.structure.placement.ExactPositionStructurePlacement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.AbstractSpreadingStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;

import java.util.Optional;

public class NarakaStructureSets {
    public static final ResourceKey<StructureSet> HEROBRINE_SANCTUARY = create("herobrine_sanctuary");
    public static final ResourceKey<StructureSet> NARAKA_PLATFORM = create("naraka_platform");

    @SuppressWarnings("deprecation")
    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<StructureSet> structureSets = context.lookup(Registries.STRUCTURE_SET);
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(
                HEROBRINE_SANCTUARY,
                new StructureSet(
                        structures.getOrThrow(NarakaStructures.HEROBRINE_SANCTUARY),
                        new ConcentricRingsStructurePlacement(
                                Vec3i.ZERO,
                                AbstractSpreadingStructurePlacement.FrequencyReductionMethod.DEFAULT,
                                1.0F,
                                8927916,
                                Optional.of(new AbstractSpreadingStructurePlacement.ExclusionZone(structureSets.getOrThrow(BuiltinStructureSets.VILLAGES), 12)),
                                32,
                                3,
                                128,
                                biomes.getOrThrow(NarakaBiomeTags.HEROBRINE_SANCTUARY_BIOMES)
                        )
                )
        );
        context.register(
                NARAKA_PLATFORM,
                new StructureSet(
                        structures.getOrThrow(NarakaStructures.NARAKA_PLATFORM),
                        new ExactPositionStructurePlacement(0, 0)
                )
        );
    }

    private static ResourceKey<StructureSet> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, NarakaMod.identifier(name));
    }
}

package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class NarakaStructureSets {
    public static final ResourceKey<StructureSet> HEROBRINE_SANCTUARY = create("herobrine_sanctuary");

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(
                HEROBRINE_SANCTUARY,
                new StructureSet(
                        structures.getOrThrow(NarakaStructures.HEROBRINE_SANCTUARY),
                        new RandomSpreadStructurePlacement(80, 20, RandomSpreadType.LINEAR, 10372)
                )
        );
    }

    private static ResourceKey<StructureSet> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, NarakaMod.location(name));
    }
}

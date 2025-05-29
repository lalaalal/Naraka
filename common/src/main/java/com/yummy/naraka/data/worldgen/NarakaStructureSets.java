package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.NarakaStructureSetTags;
import com.yummy.naraka.world.structure.placement.ExclusiveRandomSpreadStructurePlacement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class NarakaStructureSets {
    public static final ResourceKey<StructureSet> HEROBRINE_SANCTUARY = create("herobrine_sanctuary");

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<StructureSet> structureSets = context.lookup(Registries.STRUCTURE_SET);
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(
                HEROBRINE_SANCTUARY,
                new StructureSet(
                        structures.getOrThrow(NarakaStructures.HEROBRINE_SANCTUARY),
                        new ExclusiveRandomSpreadStructurePlacement(
                                32,
                                80,
                                20,
                                RandomSpreadType.TRIANGULAR,
                                structureSets.getOrThrow(NarakaStructureSetTags.HEROBRINE_SANCTUARY_EXCLUSIVE),
                                12,
                                8927916
                        )
                )
        );
    }

    private static ResourceKey<StructureSet> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, NarakaMod.location(name));
    }
}

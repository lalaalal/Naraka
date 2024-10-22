package com.yummy.naraka.world.structure.placement;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class NarakaStructurePlacementTypes {
    public static final StructurePlacementType<ExclusiveRandomSpreadStructurePlacement> EXCLUSIVE_RANDOM_SPREAD = register(
            "exclusive_random_spread",
            () -> ExclusiveRandomSpreadStructurePlacement.CODEC
    );

    private static <T extends StructurePlacement> StructurePlacementType<T> register(String name, StructurePlacementType<T> type) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, NarakaMod.location(name), type);
    }

    public static void initialize() {

    }
}

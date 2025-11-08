package com.yummy.naraka.world.structure.placement;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class NarakaStructurePlacementTypes {
    public static final HolderProxy<StructurePlacementType<?>, StructurePlacementType<ExclusiveRandomSpreadStructurePlacement>> EXCLUSIVE_RANDOM_SPREAD = register(
            "exclusive_random_spread",
            () -> ExclusiveRandomSpreadStructurePlacement.CODEC
    );

    public static final HolderProxy<StructurePlacementType<?>, StructurePlacementType<ExactPositionStructurePlacement>> EXACT_POSITION = register(
            "exact_position",
            () -> ExactPositionStructurePlacement.CODEC
    );

    private static <T extends StructurePlacement> HolderProxy<StructurePlacementType<?>, StructurePlacementType<T>> register(String name, StructurePlacementType<T> type) {
        return RegistryProxy.register(Registries.STRUCTURE_PLACEMENT, name, () -> type);
    }

    public static void initialize() {

    }
}

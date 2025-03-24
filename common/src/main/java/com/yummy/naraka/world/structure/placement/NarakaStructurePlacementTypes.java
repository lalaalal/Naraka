package com.yummy.naraka.world.structure.placement;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class NarakaStructurePlacementTypes {
    public static final LazyHolder<StructurePlacementType<?>, StructurePlacementType<ExclusiveRandomSpreadStructurePlacement>> EXCLUSIVE_RANDOM_SPREAD = register(
            "exclusive_random_spread",
            () -> ExclusiveRandomSpreadStructurePlacement.CODEC
    );

    private static <T extends StructurePlacement> LazyHolder<StructurePlacementType<?>, StructurePlacementType<T>> register(String name, StructurePlacementType<T> type) {
        return RegistryProxy.register(Registries.STRUCTURE_PLACEMENT, name, () -> type);
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.STRUCTURE_PLACEMENT)
                .onRegistrationFinished();
    }
}

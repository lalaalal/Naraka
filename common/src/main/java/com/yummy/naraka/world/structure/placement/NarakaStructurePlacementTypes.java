package com.yummy.naraka.world.structure.placement;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

public class NarakaStructurePlacementTypes {
    public static final HolderProxy<MapCodec<? extends StructurePlacement>, MapCodec<ExclusiveRandomSpreadStructurePlacement>> EXCLUSIVE_RANDOM_SPREAD = register(
            "exclusive_random_spread",
            ExclusiveRandomSpreadStructurePlacement.CODEC
    );

    public static final HolderProxy<MapCodec<? extends StructurePlacement>, MapCodec<ExactPositionStructurePlacement>> EXACT_POSITION = register(
            "exact_position",
            ExactPositionStructurePlacement.CODEC
    );

    private static <T extends StructurePlacement> HolderProxy<MapCodec<? extends StructurePlacement>, MapCodec<T>> register(String name, MapCodec<T> type) {
        return RegistryProxy.register(Registries.STRUCTURE_PLACEMENT, name, () -> type);
    }

    public static void initialize() {

    }
}

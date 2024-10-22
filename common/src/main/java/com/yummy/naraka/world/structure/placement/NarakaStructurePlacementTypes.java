package com.yummy.naraka.world.structure.placement;

import com.yummy.naraka.NarakaMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class NarakaStructurePlacementTypes {
    private static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, Registries.STRUCTURE_PLACEMENT);

    public static final RegistrySupplier<StructurePlacementType<ExclusiveRandomSpreadStructurePlacement>> EXCLUSIVE_RANDOM_SPREAD = register(
            "exclusive_random_spread",
            () -> ExclusiveRandomSpreadStructurePlacement.CODEC
    );

    private static <T extends StructurePlacement> RegistrySupplier<StructurePlacementType<T>> register(String name, StructurePlacementType<T> type) {
        return STRUCTURE_PLACEMENT_TYPES.register(name, () -> type);
    }

    public static void initialize() {
        STRUCTURE_PLACEMENT_TYPES.register();
    }
}

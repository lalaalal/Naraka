package com.yummy.naraka.world.structure.placement;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaStructurePlacementTypes {
    private static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, NarakaMod.MOD_ID);

    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<ExclusiveRandomSpreadStructurePlacement>> EXCLUSIVE_RANDOM_SPREAD = STRUCTURE_PLACEMENT_TYPES.register(
            "exclusive_random_spread",
            () -> () -> ExclusiveRandomSpreadStructurePlacement.CODEC
    );

    public static void register(IEventBus bus) {
        STRUCTURE_PLACEMENT_TYPES.register(bus);
    }
}

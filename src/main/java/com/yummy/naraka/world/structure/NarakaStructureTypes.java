package com.yummy.naraka.world.structure;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaStructureTypes {
    private static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, NarakaMod.MOD_ID);

    public static final DeferredHolder<StructureType<?>, StructureType<JumboStructure>> JUMBO = STRUCTURES.register(
            "jumbo_structure",
            () -> () -> JumboStructure.CODEC
    );

    public static void register(IEventBus bus) {
        STRUCTURES.register(bus);
    }
}

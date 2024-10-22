package com.yummy.naraka.world.structure;

import com.yummy.naraka.NarakaMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class NarakaStructureTypes {
    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, Registries.STRUCTURE_TYPE);

    public static final RegistrySupplier<StructureType<JumboStructure>> JUMBO = register(
            "jumbo_structure", () -> JumboStructure.CODEC
    );

    private static <T extends Structure> RegistrySupplier<StructureType<T>> register(String name, StructureType<T> type) {
        return STRUCTURE_TYPES.register(name, () -> type);
    }

    public static void initialize() {
        STRUCTURE_TYPES.register();
    }
}

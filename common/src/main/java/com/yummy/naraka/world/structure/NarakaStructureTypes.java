package com.yummy.naraka.world.structure;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.RegistryInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class NarakaStructureTypes {
    public static final LazyHolder<StructureType<?>, StructureType<JumboStructure>> JUMBO = register(
            "jumbo_structure", () -> JumboStructure.CODEC
    );

    private static <T extends Structure> LazyHolder<StructureType<?>, StructureType<T>> register(String name, StructureType<T> type) {
        return RegistryProxy.register(Registries.STRUCTURE_TYPE, name, () -> type);
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.STRUCTURE_TYPE)
                .onRegistrationFinished();
    }
}

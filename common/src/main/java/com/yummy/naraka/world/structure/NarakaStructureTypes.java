package com.yummy.naraka.world.structure;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class NarakaStructureTypes {
    public static final StructureType<JumboStructure> JUMBO = register(
            "jumbo_structure", () -> JumboStructure.CODEC
    );

    private static <T extends Structure> StructureType<T> register(String name, StructureType<T> type) {
        return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, NarakaMod.location(name), type);
    }

    public static void initialize() {

    }
}

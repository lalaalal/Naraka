package com.yummy.naraka.core;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.structure.StructurePieceFactory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class NarakaRegistries {
    public static final ResourceKey<Registry<StructurePieceFactory>> STRUCTURE_PIECE_FACTORY = create("structure_piece_factory");

    private static <T> ResourceKey<Registry<T>> create(String name) {
        return ResourceKey.createRegistryKey(NarakaMod.location(name));
    }
}

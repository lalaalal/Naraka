package com.yummy.naraka.core;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.structure.height.HeightProviderType;
import com.yummy.naraka.world.structure.piece.StructurePieceFactory;
import com.yummy.naraka.world.structure.protection.ProtectionPredicate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class NarakaRegistries {
    public static final ResourceKey<Registry<StructurePieceFactory>> STRUCTURE_PIECE_FACTORY = create("structure_piece_factory");
    public static final ResourceKey<Registry<HeightProviderType>> HEIGHT_PROVIDER_TYPE = create("height_provider_type");
    public static final ResourceKey<Registry<ProtectionPredicate>> PROTECTION_PREDICATE = create("protection_predicate");

    private static <T> ResourceKey<Registry<T>> create(String name) {
        return ResourceKey.createRegistryKey(NarakaMod.location(name));
    }
}

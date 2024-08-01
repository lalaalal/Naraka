package com.yummy.naraka.core.registries;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.data.EntityDataType;
import com.yummy.naraka.world.structure.height.HeightProviderType;
import com.yummy.naraka.world.structure.piece.StructurePieceFactory;
import com.yummy.naraka.world.structure.protection.ProtectionPredicate;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class NarakaRegistries {
    public static final Registry<StructurePieceFactory> STRUCTURE_PIECE_FACTORY = create("structure_piece_factory");
    public static final Registry<HeightProviderType> HEIGHT_PROVIDER_TYPE = create("height_provider_type");
    public static final Registry<ProtectionPredicate> PROTECTION_PREDICATE = create("protection_predicate");
    public static final Registry<EntityDataType<?>> ENTITY_DATA_TYPE = create("entity_data_type");

    private static <T> Registry<T> create(String name) {
        ResourceKey<Registry<T>> key = ResourceKey.createRegistryKey(NarakaMod.location(name));
        return FabricRegistryBuilder.createSimple(key)
                .attribute(RegistryAttribute.SYNCED)
                .buildAndRegister();
    }

    public static void initialize() {

    }
}

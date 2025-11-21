package com.yummy.naraka.core.registries;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.data.EntityDataType;
import com.yummy.naraka.world.item.equipmentset.EquipmentSet;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import com.yummy.naraka.world.structure.generation.StructureGenerationPointProvider;
import com.yummy.naraka.world.structure.piece.StructurePieceFactory;
import com.yummy.naraka.world.structure.protection.ProtectionPredicate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class NarakaRegistries {
    public static final Registry<StructurePieceFactory> STRUCTURE_PIECE_FACTORY = RegistryFactory.create(Keys.STRUCTURE_PIECE_FACTORY);
    public static final Registry<StructureGenerationPointProvider> STRUCTURE_GENERATION_POINT_PROVIDER = RegistryFactory.create(Keys.STRUCTURE_GENERATION_POINT_PROVIDER);
    public static final Registry<ProtectionPredicate> PROTECTION_PREDICATE = RegistryFactory.create(Keys.PROTECTION_PREDICATE);
    public static final Registry<EntityDataType<?, ?>> ENTITY_DATA_TYPE = RegistryFactory.create(Keys.ENTITY_DATA_TYPE);
    public static final Registry<ReinforcementEffect> REINFORCEMENT_EFFECT = RegistryFactory.create(Keys.REINFORCEMENT_EFFECT);
    public static final Registry<EquipmentSet> EQUIPMENT_SET = RegistryFactory.create(Keys.EQUIPMENT_SET);

    public static void initialize() {

    }

    public static class Keys {
        public static final ResourceKey<Registry<StructurePieceFactory>> STRUCTURE_PIECE_FACTORY = create("structure_piece_factory");
        public static final ResourceKey<Registry<StructureGenerationPointProvider>> STRUCTURE_GENERATION_POINT_PROVIDER = create("structure_generation_point_provider");
        public static final ResourceKey<Registry<ProtectionPredicate>> PROTECTION_PREDICATE = create("protection_predicate");
        public static final ResourceKey<Registry<EntityDataType<?, ?>>> ENTITY_DATA_TYPE = create("entity_data_type");
        public static final ResourceKey<Registry<ReinforcementEffect>> REINFORCEMENT_EFFECT = create("reinforcement_effect");
        public static final ResourceKey<Registry<EquipmentSet>> EQUIPMENT_SET = create("equipment_set");

        private static <T> ResourceKey<Registry<T>> create(String name) {
            return ResourceKey.createRegistryKey(NarakaMod.location(name));
        }
    }
}

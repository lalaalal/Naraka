package com.yummy.naraka;

import com.yummy.naraka.advancements.NarakaCriteriaTriggers;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.init.RegistryInitializer;
import com.yummy.naraka.network.NarakaNetworks;
import com.yummy.naraka.sounds.NarakaSoundEvents;
import com.yummy.naraka.world.NarakaBiomes;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.block.grower.NarakaTreeGrowers;
import com.yummy.naraka.world.carver.NarakaWorldCarvers;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.NarakaSpawnPlacements;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import com.yummy.naraka.world.features.NarakaFeatures;
import com.yummy.naraka.world.inventory.NarakaMenuTypes;
import com.yummy.naraka.world.item.NarakaArmorMaterials;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.crafting.NarakaRecipeSerializers;
import com.yummy.naraka.world.item.crafting.NarakaRecipeTypes;
import com.yummy.naraka.world.item.equipmentset.NarakaEquipmentSets;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.rootplacer.NarakaRootPlacerTypes;
import com.yummy.naraka.world.structure.NarakaStructureTypes;
import com.yummy.naraka.world.structure.height.NarakaHeightProviders;
import com.yummy.naraka.world.structure.piece.NarakaStructurePieceFactories;
import com.yummy.naraka.world.structure.piece.NarakaStructurePieceTypes;
import com.yummy.naraka.world.structure.placement.NarakaStructurePlacementTypes;
import com.yummy.naraka.world.structure.protection.NarakaProtectionPredicates;
import net.minecraft.resources.ResourceLocation;

public final class NarakaMod {
    public static final String MOD_ID = "naraka";
    public static boolean isDataGeneration = false;
    public static boolean isRegistryLoaded = false;

    public static void initialize(NarakaInitializer initializer) {
        Platform.initialize(initializer);
        RegistryInitializer.initialize(initializer);

        NarakaConfig.load();

        RegistryFactory.initialize(initializer);
        NarakaRegistries.initialize();

        NarakaCriteriaTriggers.initialize();

        NarakaParticleTypes.initialize();
        NarakaSoundEvents.initialize();

        NarakaEntityTypes.initialize();
        NarakaEntityDataTypes.initialize(initializer);
        NarakaMobEffects.initialize();
        initializer.runAfterRegistryLoaded(NarakaSpawnPlacements::initialize);

        NarakaBlocks.initialize();
        NarakaTreeGrowers.initialize();

        NarakaReinforcementEffects.initialize();
        NarakaArmorMaterials.initialize();
        NarakaDataComponentTypes.initialize();
        NarakaItems.initialize();
        NarakaRecipeTypes.initialize();
        NarakaRecipeSerializers.initialize();
        NarakaEquipmentSets.initialize();

        NarakaCreativeModTabs.initialize(initializer);

        NarakaBlockEntityTypes.initialize();

        NarakaMenuTypes.initialize();

        NarakaStructureTypes.initialize();
        NarakaStructurePieceTypes.initialize();
        NarakaStructurePieceFactories.initialize();
        NarakaHeightProviders.initialize();
        NarakaStructurePlacementTypes.initialize();
        NarakaProtectionPredicates.initialize();

        NarakaWorldCarvers.initialize();
        NarakaFeatures.initialize();
        NarakaBiomes.initialize(initializer);

        NarakaRootPlacerTypes.initialize();

        NarakaGameEvents.initialize();
        NarakaNetworks.initialize();
    }

    public static NarakaConfig config() {
        return NarakaConfig.INSTANCE;
    }

    public static ResourceLocation mcLocation(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    /**
     * Returns mod's resource location
     *
     * @param path Resource path
     * @return {@linkplain ResourceLocation} with namespace {@linkplain #MOD_ID}
     */
    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static ResourceLocation location(String prefix, String path) {
        return location("%s/%s".formatted(prefix, path));
    }
}

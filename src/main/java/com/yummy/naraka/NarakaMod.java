package com.yummy.naraka;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.event.NarakaGameEvents;
import com.yummy.naraka.network.NarakaNetworks;
import com.yummy.naraka.sounds.NarakaSoundEvents;
import com.yummy.naraka.world.NarakaBiomes;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.block.grower.NarakaTreeGrowers;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.data.EntityDataTypes;
import com.yummy.naraka.world.inventory.NarakaMenuTypes;
import com.yummy.naraka.world.item.NarakaArmorMaterials;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.crafting.NarakaRecipeSerializers;
import com.yummy.naraka.world.item.crafting.NarakaRecipeTypes;
import com.yummy.naraka.world.rootplacers.NarakaRootPlacerTypes;
import com.yummy.naraka.world.structure.NarakaStructureTypes;
import com.yummy.naraka.world.structure.height.NarakaHeightProviders;
import com.yummy.naraka.world.structure.piece.NarakaStructurePieceFactories;
import com.yummy.naraka.world.structure.piece.NarakaStructurePieceTypes;
import com.yummy.naraka.world.structure.placement.NarakaStructurePlacementTypes;
import com.yummy.naraka.world.structure.protection.NarakaProtectionPredicates;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class NarakaMod implements ModInitializer {
    public static final String MOD_ID = "naraka";

    @Override
    public void onInitialize() {
        NarakaRegistries.initialize();
        NarakaParticleTypes.initialize();

        NarakaSoundEvents.initialize();

        NarakaBlocks.initialize();
        NarakaTreeGrowers.initialize();

        NarakaItems.initialize();
        NarakaDataComponentTypes.initialize();
        NarakaArmorMaterials.initialize();
        NarakaCreativeModTabs.initialize();
        NarakaRecipeTypes.initialize();
        NarakaRecipeSerializers.initialize();

        NarakaEntityTypes.initialize();
        EntityDataTypes.initialize();

        NarakaBlockEntityTypes.initialize();

        NarakaMenuTypes.initialize();

        NarakaStructureTypes.initialize();
        NarakaStructurePieceTypes.initialize();
        NarakaStructurePieceFactories.initialize();
        NarakaHeightProviders.initialize();
        NarakaStructurePlacementTypes.initialize();
        NarakaProtectionPredicates.initialize();

        NarakaBiomes.initialize();

        NarakaRootPlacerTypes.initialize();

        NarakaGameEvents.initialize();
        NarakaContext.initialize();
        NarakaNetworks.initialize();
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

package com.yummy.naraka;

import com.yummy.naraka.core.NarakaRegistries;
import com.yummy.naraka.event.NarakaGameEvents;
import com.yummy.naraka.world.NarakaBiomes;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.inventory.NarakaMenuTypes;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.crafting.NarakaRecipeSerializers;
import com.yummy.naraka.world.item.crafting.NarakaRecipeTypes;
import com.yummy.naraka.world.structure.NarakaStructureTypes;
import com.yummy.naraka.world.structure.height.NarakaHeightProviders;
import com.yummy.naraka.world.structure.piece.NarakaStructurePieceFactories;
import com.yummy.naraka.world.structure.piece.NarakaStructurePieceTypes;
import com.yummy.naraka.world.structure.placement.NarakaStructurePlacementTypes;
import com.yummy.naraka.world.structure.protection.NarakaProtectionPredicates;
import com.yummy.naraka.world.trunkplacers.NarakaTruncPlacerTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class NarakaMod implements ModInitializer {
    public static final String MOD_ID = "naraka";

    @Override
    public void onInitialize() {
        NarakaRegistries.initialize();
        NarakaBlocks.initialize();
        NarakaItems.initialize();
        NarakaCreativeModTabs.initialize();
        NarakaEntityTypes.initialize();
        NarakaBlockEntityTypes.initialize();
        NarakaMenuTypes.initialize();
        NarakaRecipeTypes.initialize();
        NarakaRecipeSerializers.initialize();
        NarakaHeightProviders.initialize();
        NarakaStructurePlacementTypes.initialize();
        NarakaStructureTypes.initialize();
        NarakaStructurePieceTypes.initialize();
        NarakaStructurePieceFactories.initialize();
        NarakaProtectionPredicates.initialize();
        NarakaBiomes.initialize();
        NarakaTruncPlacerTypes.initialize();

        NarakaGameEvents.initialize();
        NarakaContext.initialize();
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

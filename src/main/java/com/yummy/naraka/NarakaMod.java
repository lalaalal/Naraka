package com.yummy.naraka;

import com.yummy.naraka.core.NarakaRegistries;
import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.HerobrineTotemBlockEntity;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.entity.Herobrine;
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
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;

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

        NarakaContext.initialize();
        NarakaBlocks.setFlammableBlocks();

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.NECTARIUM_ORE_BURIED_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.NECTARIUM_ORE_SMALL_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.NECTARIUM_ORE_LARGE_PLACED_KEY);

        FabricDefaultAttributeRegistry.register(NarakaEntityTypes.HEROBRINE, Herobrine.getAttributeSupplier());

        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            ItemStack item = player.getUseItem();
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);

            if (hitResult.getDirection() == Direction.UP
                    && item.is(ItemTags.CREEPER_IGNITERS) && state.is(Blocks.NETHERRACK)
                    && HerobrineTotemBlockEntity.isTotemStructure(level, pos.below())) {
                BlockState totem = level.getBlockState(pos.below());
                if (HerobrineTotemBlockEntity.isSleeping(totem))
                    HerobrineTotem.crack(level, pos.below(), totem);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
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
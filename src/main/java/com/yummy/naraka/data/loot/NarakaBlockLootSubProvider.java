package com.yummy.naraka.data.loot;

import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Collections;

public class NarakaBlockLootSubProvider extends BlockLootSubProvider {
    protected NarakaBlockLootSubProvider(HolderLookup.Provider provider) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        dropSelf(NarakaBlocks.NECTARIUM_BLOCK);
        dropOre(NarakaBlocks.NECTARIUM_ORE, NarakaItems.NECTARIUM);
        dropOre(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, NarakaItems.NECTARIUM);
        dropSelf(NarakaBlocks.SOUL_CRAFTING_BLOCK);

        dropSelf(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK);
        dropSelf(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK);
        dropSelf(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK);
        dropSelf(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK);
        dropSelf(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK);
        dropSelf(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK);
        dropSelf(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK);
        dropSelf(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK);
        dropSelf(NarakaBlocks.PURIFIED_SOUL_BLOCK);
        dropSelf(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK);

        dropSelf(NarakaBlocks.EBONY_LOG);
        dropSelf(NarakaBlocks.STRIPPED_EBONY_LOG);
        dropSelf(NarakaBlocks.EBONY_WOOD);
        dropSelf(NarakaBlocks.STRIPPED_EBONY_WOOD);

        add(NarakaBlocks.EBONY_LEAVES.get(), createLeavesDrops(NarakaBlocks.EBONY_LEAVES.get(), NarakaBlocks.EBONY_SAPLING.get(), 0.2f));
        dropSelf(NarakaBlocks.EBONY_SAPLING);

        dropOther(NarakaBlocks.EBONY_SIGN.get(), NarakaItems.EBONY_SIGN);
        dropOther(NarakaBlocks.EBONY_WALL_SIGN.get(), NarakaItems.EBONY_SIGN);
        dropOther(NarakaBlocks.EBONY_HANGING_SIGN.get(), NarakaItems.EBONY_HANGING_SIGN);
        dropOther(NarakaBlocks.EBONY_WALL_HANGING_SIGN.get(), NarakaItems.EBONY_HANGING_SIGN);

        dropSelf(NarakaBlocks.EBONY_PLANKS);
        add(NarakaBlocks.EBONY_SLAB.get(), createSlabItemTable(NarakaBlocks.EBONY_SLAB.get()));
        dropSelf(NarakaBlocks.EBONY_STAIRS);
    }

    protected void dropSelf(DeferredBlock<? extends Block> block) {
        super.dropSelf(block.get());
    }

    protected void dropOre(DeferredBlock<? extends Block> oreBlock, DeferredItem<? extends Item> itemSupplier) {
        add(oreBlock.get(), block -> createOreDrop(block, itemSupplier.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return NarakaBlocks.getKnownBlocks();
    }
}

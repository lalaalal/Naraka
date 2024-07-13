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
        dropSelf(NarakaBlocks.COMPRESSED_IRON_BLOCK);
        dropSelf(NarakaBlocks.FAKE_GOLD_BLOCK);
        dropSelf(NarakaBlocks.AMETHYST_SHARD_BLOCK);

        NarakaBlocks.forEachSoulInfusedBlock(this::dropSelf);
        dropSelf(NarakaBlocks.PURIFIED_SOUL_BLOCK);
        dropSelf(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK);

        dropSelf(NarakaBlocks.EBONY_LOG);
        dropSelf(NarakaBlocks.STRIPPED_EBONY_LOG);
        dropSelf(NarakaBlocks.EBONY_WOOD);
        dropSelf(NarakaBlocks.STRIPPED_EBONY_WOOD);
        dropSelf(NarakaBlocks.HARD_EBONY_PLANKS);

        add(NarakaBlocks.EBONY_LEAVES.get(), createLeavesDrops(NarakaBlocks.EBONY_LEAVES.get(), NarakaBlocks.EBONY_SAPLING.get(), 0.01f));
        dropSelf(NarakaBlocks.EBONY_SAPLING);

        dropOther(NarakaBlocks.EBONY_SIGN.get(), NarakaItems.EBONY_SIGN);
        dropOther(NarakaBlocks.EBONY_WALL_SIGN.get(), NarakaItems.EBONY_SIGN);
        dropOther(NarakaBlocks.EBONY_HANGING_SIGN.get(), NarakaItems.EBONY_HANGING_SIGN);
        dropOther(NarakaBlocks.EBONY_WALL_HANGING_SIGN.get(), NarakaItems.EBONY_HANGING_SIGN);

        dropSelf(NarakaBlocks.EBONY_PLANKS);
        add(NarakaBlocks.EBONY_SLAB.get(), createSlabItemTable(NarakaBlocks.EBONY_SLAB.get()));
        dropSelf(NarakaBlocks.EBONY_STAIRS);
        dropSelf(NarakaBlocks.EBONY_FENCE);
        dropSelf(NarakaBlocks.EBONY_FENCE_GATE);
        add(NarakaBlocks.EBONY_DOOR.get(), createDoorTable(NarakaBlocks.EBONY_DOOR.get()));
        dropSelf(NarakaBlocks.EBONY_TRAPDOOR);
        dropSelf(NarakaBlocks.EBONY_PRESSURE_PLATE);
        dropSelf(NarakaBlocks.EBONY_BUTTON);
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

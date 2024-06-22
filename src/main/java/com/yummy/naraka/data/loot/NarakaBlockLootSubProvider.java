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
        dropSelf(NarakaBlocks.PURIFIED_SOUL_BLOCK);
        dropSelf(NarakaBlocks.NECTARIUM_BLOCK);
        dropOre(NarakaBlocks.NECTARIUM_ORE, NarakaItems.NECTARIUM);
        dropOre(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, NarakaItems.NECTARIUM);
        dropSelf(NarakaBlocks.SOUL_CRAFTING_BLOCK);
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

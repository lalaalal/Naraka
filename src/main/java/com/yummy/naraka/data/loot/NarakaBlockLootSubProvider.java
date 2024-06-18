package com.yummy.naraka.data.loot;

import com.yummy.naraka.block.NarakaBlocks;
import com.yummy.naraka.item.NarakaItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.function.Supplier;

public class NarakaBlockLootSubProvider extends BlockLootSubProvider {
    protected NarakaBlockLootSubProvider(HolderLookup.Provider provider) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        noDrop(NarakaBlocks.TRANSPARENT_BLOCK);
        dropSelf(NarakaBlocks.PURIFIED_SOUL_BLOCK);
        dropSelf(NarakaBlocks.NECTARIUM_BLOCK);
        dropOre(NarakaBlocks.NECTARIUM_ORE, NarakaItems.NECTARIUM);
        dropOre(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, NarakaItems.NECTARIUM);
    }

    protected void noDrop(Supplier<? extends Block> block) {
        add(block.get(), noDrop());
    }

    protected void dropSelf(Supplier<? extends Block> block) {
        super.dropSelf(block.get());
    }

    protected void dropOre(Supplier<? extends Block> blockSupplier, Supplier<? extends Item> itemSupplier) {
        add(blockSupplier.get(), block -> createOreDrop(block, itemSupplier.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return NarakaBlocks.getKnownBlocks();
    }
}

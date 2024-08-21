package com.yummy.naraka.data.loot;

import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class NarakaBlockLootProvider extends FabricBlockLootTableProvider {
    public NarakaBlockLootProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(NarakaBlocks.NECTARIUM_BLOCK);
        dropOre(NarakaBlocks.NECTARIUM_ORE, NarakaItems.NECTARIUM);
        dropOre(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, NarakaItems.NECTARIUM);
        dropSelf(NarakaBlocks.SOUL_CRAFTING_BLOCK);
        dropSelf(NarakaBlocks.FORGING_BLOCK);
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

        add(NarakaBlocks.EBONY_LEAVES, createLeavesDrops(NarakaBlocks.EBONY_LEAVES, NarakaBlocks.EBONY_SAPLING, 0.01f));
        dropSelf(NarakaBlocks.EBONY_SAPLING);
        dropSelf(NarakaBlocks.HEROBRINE_TOTEM);
        dropPottedContents(NarakaBlocks.POTTED_EBONY_SAPLING);
        dropSelf(NarakaBlocks.EBONY_ROOTS);
        dropSelf(NarakaBlocks.EBONY_METAL_BLOCK);
    }

    protected void dropOre(Block oreBlock, Item item) {
        add(oreBlock, block -> createOreDrop(block, item));
    }
}

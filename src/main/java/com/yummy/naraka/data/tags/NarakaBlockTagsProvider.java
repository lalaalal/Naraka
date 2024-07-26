package com.yummy.naraka.data.tags;

import com.yummy.naraka.tags.NarakaBlockTags;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class NarakaBlockTagsProvider extends FabricTagProvider<Block> {
    public NarakaBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_GOLD_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_STONE_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_WOODEN_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        getOrCreateTagBuilder(NarakaBlockTags.NECTARIUM_ORES)
                .add(NarakaBlocks.NECTARIUM_ORE)
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE);
        getOrCreateTagBuilder(ConventionalBlockTags.ORES)
                .addTag(NarakaBlockTags.NECTARIUM_ORES);

        getOrCreateTagBuilder(BlockTags.FIRE)
                .add(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK);

        getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
                .add(NarakaBlocks.POTTED_EBONY_SAPLING);
        getOrCreateTagBuilder(BlockTags.SAPLINGS)
                .add(NarakaBlocks.EBONY_SAPLING);

        getOrCreateTagBuilder(NarakaBlockTags.EBONY_LOGS)
                .add(NarakaBlocks.EBONY_LOG)
                .add(NarakaBlocks.EBONY_WOOD)
                .add(NarakaBlocks.STRIPPED_EBONY_LOG)
                .add(NarakaBlocks.STRIPPED_EBONY_WOOD);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .addTag(NarakaBlockTags.EBONY_LOGS);
        getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(NarakaBlocks.HARD_EBONY_PLANKS);

        getOrCreateTagBuilder(NarakaBlockTags.HEROBRINE_SANCTUARY_WRAP_TARGETS)
                .add(Blocks.WATER)
                .add(Blocks.GRAVEL)
                .forceAddTag(BlockTags.SAND);

        FabricTagBuilder needsIronTool = getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(NarakaBlocks.NECTARIUM_BLOCK)
                .add(NarakaBlocks.NECTARIUM_ORE)
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE)
                .add(NarakaBlocks.FAKE_GOLD_BLOCK)
                .add(NarakaBlocks.COMPRESSED_IRON_BLOCK)
                .addTag(NarakaBlockTags.EBONY_LOGS)
                .add(NarakaBlocks.HARD_EBONY_PLANKS);
        NarakaBlocks.forEachSoulInfusedBlock(needsIronTool::add);

        getOrCreateTagBuilder(NarakaBlockTags.NEEDS_NETHERITE_TOOL)
                .add(NarakaBlocks.HEROBRINE_TOTEM)
                .add(NarakaBlocks.PURIFIED_SOUL_BLOCK)
                .add(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK);
        FabricTagBuilder mineableWithPickaxe = getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(NarakaBlocks.HEROBRINE_TOTEM)
                .add(NarakaBlocks.AMETHYST_SHARD_BLOCK)
                .add(NarakaBlocks.NECTARIUM_BLOCK)
                .add(NarakaBlocks.NECTARIUM_ORE)
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE)
                .add(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK)
                .add(NarakaBlocks.FAKE_GOLD_BLOCK)
                .add(NarakaBlocks.COMPRESSED_IRON_BLOCK);
        NarakaBlocks.forEachSoulInfusedBlock(mineableWithPickaxe::add);

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_HOE)
                .add(NarakaBlocks.PURIFIED_SOUL_BLOCK)
                .add(NarakaBlocks.EBONY_LEAVES);

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
                .addTag(NarakaBlockTags.EBONY_LOGS)
                .add(NarakaBlocks.HARD_EBONY_PLANKS);
    }
}

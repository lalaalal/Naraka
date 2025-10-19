package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.tags.NarakaBlockTags;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class NarakaBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public NarakaBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        valueLookupBuilder(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        valueLookupBuilder(BlockTags.INCORRECT_FOR_GOLD_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        valueLookupBuilder(BlockTags.INCORRECT_FOR_STONE_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        valueLookupBuilder(BlockTags.INCORRECT_FOR_WOODEN_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        valueLookupBuilder(NarakaBlockTags.NECTARIUM_ORES)
                .add(NarakaBlocks.NECTARIUM_ORE.get())
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get());
        valueLookupBuilder(NarakaBlockTags.AMETHYST_ORES)
                .add(NarakaBlocks.AMETHYST_ORE.get())
                .add(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get());
        valueLookupBuilder(ConventionalTags.Blocks.ORES)
                .addTag(NarakaBlockTags.NECTARIUM_ORES)
                .addTag(NarakaBlockTags.AMETHYST_ORES);

        valueLookupBuilder(BlockTags.FIRE)
                .add(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get());

        valueLookupBuilder(NarakaBlockTags.HEROBRINE_SANCTUARY_AIR_WRAP_TARGETS)
                .add(Blocks.WATER)
                .add(Blocks.GRAVEL)
                .forceAddTag(BlockTags.SAND);
        valueLookupBuilder(NarakaBlockTags.HEROBRINE_SANCTUARY_LAVA_WRAP_TARGETS)
                .forceAddTag(BlockTags.AIR)
                .add(Blocks.WATER);

        valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get())
                .add(NarakaBlocks.NECTARIUM_CORE_BLOCK.get());

        TagAppender<Block, Block> needsIronTool = valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(NarakaBlocks.NECTARIUM_BLOCK.get())
                .add(NarakaBlocks.NECTARIUM_ORE.get())
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get())
                .add(NarakaBlocks.IMITATION_GOLD_BLOCK.get())
                .add(NarakaBlocks.COMPRESSED_IRON_BLOCK.get());
        NarakaBlocks.forEachSoulInfusedBlock(needsIronTool::add);

        valueLookupBuilder(NarakaBlockTags.NEEDS_NETHERITE_TOOL)
                .add(NarakaBlocks.HEROBRINE_TOTEM.get())
                .add(NarakaBlocks.PURIFIED_SOUL_BLOCK.get())
                .add(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get());
        TagAppender<Block, Block> mineableWithPickaxe = valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(NarakaBlocks.HEROBRINE_TOTEM.get())
                .add(NarakaBlocks.AMETHYST_SHARD_BLOCK.get())
                .add(NarakaBlocks.NECTARIUM_BLOCK.get())
                .add(NarakaBlocks.NECTARIUM_ORE.get())
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get())
                .add(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get())
                .add(NarakaBlocks.IMITATION_GOLD_BLOCK.get())
                .add(NarakaBlocks.COMPRESSED_IRON_BLOCK.get())
                .add(NarakaBlocks.NECTARIUM_CORE_BLOCK.get())
                .add(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get())
                .add(NarakaBlocks.AMETHYST_ORE.get())
                .add(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get());
        NarakaBlocks.forEachSoulInfusedBlock(mineableWithPickaxe::add);

        valueLookupBuilder(BlockTags.MINEABLE_WITH_HOE)
                .add(NarakaBlocks.PURIFIED_SOUL_BLOCK.get());

        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE)
                .add(NarakaBlocks.SOUL_SMITHING_BLOCK.get());
    }
}

package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.tags.NarakaBlockTags;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.references.BlockIds;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class NarakaBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public NarakaBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        builder(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        builder(BlockTags.INCORRECT_FOR_GOLD_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        builder(BlockTags.INCORRECT_FOR_STONE_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        builder(BlockTags.INCORRECT_FOR_WOODEN_TOOL)
                .addTag(NarakaBlockTags.NEEDS_NETHERITE_TOOL);
        builder(NarakaBlockTags.NECTARIUM_ORES)
                .add(NarakaBlocks.NECTARIUM_ORE.key())
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.key());
        builder(NarakaBlockTags.AMETHYST_ORES)
                .add(NarakaBlocks.AMETHYST_ORE.key())
                .add(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.key());
        builder(ConventionalTags.Blocks.ORES)
                .addTag(NarakaBlockTags.NECTARIUM_ORES)
                .addTag(NarakaBlockTags.AMETHYST_ORES);
        builder(NarakaBlockTags.MINABLE_WITH_NARAKA_PICKAXE)
                .forceAddTag(BlockTags.MINEABLE_WITH_AXE)
                .forceAddTag(BlockTags.MINEABLE_WITH_HOE)
                .forceAddTag(BlockTags.MINEABLE_WITH_PICKAXE);

        builder(BlockTags.FIRE)
                .add(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.key());

        builder(NarakaBlockTags.HEROBRINE_SANCTUARY_AIR_WRAP_TARGETS)
                .add(BlockIds.WATER)
                .add(BlockItemIds.GRAVEL)
                .forceAddTag(BlockTags.SAND);
        builder(NarakaBlockTags.HEROBRINE_SANCTUARY_LAVA_WRAP_TARGETS)
                .forceAddTag(BlockTags.AIR)
                .add(BlockIds.WATER);

        builder(BlockTags.NEEDS_STONE_TOOL)
                .add(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.key())
                .add(NarakaBlocks.NECTARIUM_CORE_BLOCK.key());

        TagAppender<Block> needsIronTool = builder(BlockTags.NEEDS_IRON_TOOL);
        needsIronTool
                .add(NarakaBlocks.NECTARIUM_BLOCK.key())
                .add(NarakaBlocks.NECTARIUM_ORE.key())
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.key())
                .add(NarakaBlocks.IMITATION_GOLD_BLOCK.key());
        NarakaBlocks.SOUL_INFUSED_BLOCKS
                .stream()
                .map(HolderProxy::key)
                .forEach(needsIronTool::add);

        builder(NarakaBlockTags.NEEDS_NETHERITE_TOOL)
                .add(NarakaBlocks.HEROBRINE_TOTEM.key())
                .add(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.key());
        TagAppender<Block> mineableWithPickaxe = builder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(NarakaBlocks.HEROBRINE_TOTEM.key())
                .add(NarakaBlocks.AMETHYST_SHARD_BLOCK.key())
                .add(NarakaBlocks.NECTARIUM_BLOCK.key())
                .add(NarakaBlocks.NECTARIUM_ORE.key())
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.key())
                .add(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.key())
                .add(NarakaBlocks.IMITATION_GOLD_BLOCK.key())
                .add(NarakaBlocks.NECTARIUM_CORE_BLOCK.key())
                .add(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.key())
                .add(NarakaBlocks.AMETHYST_ORE.key())
                .add(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.key());
        NarakaBlocks.SOUL_INFUSED_BLOCKS
                .stream()
                .map(HolderProxy::key)
                .forEach(mineableWithPickaxe::add);

        builder(BlockTags.MINEABLE_WITH_AXE)
                .add(NarakaBlocks.SOUL_SMITHING_BLOCK.key());
    }
}

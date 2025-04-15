package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.tags.NarakaBlockTags;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class NarakaBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public NarakaBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
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
                .add(NarakaBlocks.NECTARIUM_ORE.get())
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get());
        getOrCreateTagBuilder(NarakaBlockTags.AMETHYST_ORES)
                .add(NarakaBlocks.AMETHYST_ORE.get())
                .add(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get());
        getOrCreateTagBuilder(ConventionalTags.Blocks.ORES)
                .addTag(NarakaBlockTags.NECTARIUM_ORES)
                .addTag(NarakaBlockTags.AMETHYST_ORES);

        getOrCreateTagBuilder(BlockTags.FIRE)
                .add(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get());

        getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
                .add(NarakaBlocks.POTTED_EBONY_SAPLING.get());
        getOrCreateTagBuilder(BlockTags.SAPLINGS)
                .add(NarakaBlocks.EBONY_SAPLING.get());

        getOrCreateTagBuilder(NarakaBlockTags.EBONY_LOGS)
                .add(NarakaBlocks.EBONY_LOG.get())
                .add(NarakaBlocks.EBONY_WOOD.get())
                .add(NarakaBlocks.STRIPPED_EBONY_LOG.get())
                .add(NarakaBlocks.STRIPPED_EBONY_WOOD.get());
        getOrCreateTagBuilder(NarakaBlockTags.EBONY_ROOTS_CAN_GROW_THROUGH)
                .forceAddTag(BlockTags.DIRT)
                .forceAddTag(BlockTags.BASE_STONE_OVERWORLD);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .addTag(NarakaBlockTags.EBONY_LOGS);
        getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(NarakaBlocks.HARD_EBONY_PLANKS.get());

        getOrCreateTagBuilder(NarakaBlockTags.HEROBRINE_SANCTUARY_AIR_WRAP_TARGETS)
                .add(Blocks.WATER)
                .add(Blocks.GRAVEL)
                .forceAddTag(BlockTags.SAND);
        getOrCreateTagBuilder(NarakaBlockTags.HEROBRINE_SANCTUARY_LAVA_WRAP_TARGETS)
                .forceAddTag(BlockTags.AIR)
                .add(Blocks.WATER);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get())
                .add(NarakaBlocks.NECTARIUM_CORE_BLOCK.get());

        FabricTagBuilder needsIronTool = getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(NarakaBlocks.NECTARIUM_BLOCK.get())
                .add(NarakaBlocks.NECTARIUM_ORE.get())
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get())
                .add(NarakaBlocks.IMITATION_GOLD_BLOCK.get())
                .add(NarakaBlocks.COMPRESSED_IRON_BLOCK.get())
                .addTag(NarakaBlockTags.EBONY_LOGS)
                .add(NarakaBlocks.HARD_EBONY_PLANKS.get())
                .add(NarakaBlocks.EBONY_ROOTS.get());
        NarakaBlocks.forEachSoulInfusedBlock(needsIronTool::add);

        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(NarakaBlocks.EBONY_METAL_BLOCK.get());
        getOrCreateTagBuilder(NarakaBlockTags.NEEDS_NETHERITE_TOOL)
                .add(NarakaBlocks.HEROBRINE_TOTEM.get())
                .add(NarakaBlocks.PURIFIED_SOUL_BLOCK.get())
                .add(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get());
        FabricTagBuilder mineableWithPickaxe = getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(NarakaBlocks.EBONY_METAL_BLOCK.get())
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

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_HOE)
                .add(NarakaBlocks.PURIFIED_SOUL_BLOCK.get())
                .add(NarakaBlocks.EBONY_LEAVES.get());

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
                .addTag(NarakaBlockTags.EBONY_LOGS)
                .add(NarakaBlocks.SOUL_SMITHING_BLOCK.get())
                .add(NarakaBlocks.HARD_EBONY_PLANKS.get())
                .add(NarakaBlocks.EBONY_ROOTS.get());
    }
}

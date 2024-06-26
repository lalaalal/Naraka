package com.yummy.naraka.data.tags;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.NarakaBlockTags;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class NarakaBlockTagsProvider extends BlockTagsProvider {
    public NarakaBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, NarakaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(NarakaBlockTags.NECTARIUM_ORES)
                .add(NarakaBlocks.NECTARIUM_ORE.get())
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get());
        tag(Tags.Blocks.ORES)
                .addTag(NarakaBlockTags.NECTARIUM_ORES);
        tag(Tags.Blocks.ORES_IN_GROUND_STONE)
                .add(NarakaBlocks.NECTARIUM_ORE.get());
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
                .add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get());

        tag(BlockTags.FIRE)
                .add(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get());

        tag(NarakaBlockTags.EBONY_LOGS)
                .add(NarakaBlocks.EBONY_LOG.get())
                .add(NarakaBlocks.EBONY_WOOD.get())
                .add(NarakaBlocks.STRIPPED_EBONY_LOG.get())
                .add(NarakaBlocks.STRIPPED_EBONY_WOOD.get());

        tag(BlockTags.WOODEN_STAIRS)
                .add(NarakaBlocks.EBONY_STAIRS.get());
        tag(BlockTags.WOODEN_SLABS)
                .add(NarakaBlocks.EBONY_SLAB.get());
        tag(BlockTags.WOODEN_FENCES)
                .add(NarakaBlocks.EBONY_FENCE.get());
        tag(BlockTags.WOODEN_DOORS)
                .add(NarakaBlocks.EBONY_DOOR.get());
        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(NarakaBlocks.EBONY_TRAPDOOR.get());
        tag(BlockTags.WOODEN_BUTTONS)
                .add(NarakaBlocks.EBONY_BUTTON.get());

        tag(BlockTags.LOGS_THAT_BURN)
                .addTag(NarakaBlockTags.EBONY_LOGS);
        tag(BlockTags.PLANKS)
                .add(NarakaBlocks.EBONY_PLANKS.get());
        tag(BlockTags.STANDING_SIGNS)
                .add(NarakaBlocks.EBONY_SIGN.get());
        tag(BlockTags.WALL_SIGNS)
                .add(NarakaBlocks.EBONY_WALL_SIGN.get());
        tag(BlockTags.CEILING_HANGING_SIGNS)
                .add(NarakaBlocks.EBONY_HANGING_SIGN.get());
        tag(BlockTags.WALL_HANGING_SIGNS)
                .add(NarakaBlocks.EBONY_WALL_HANGING_SIGN.get());
    }
}

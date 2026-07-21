package com.yummy.naraka.forge.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.tags.NarakaBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import java.util.concurrent.CompletableFuture;

public class NarakaForgeBlockTagsProvider extends BlockTagsProvider {
    public NarakaForgeBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, NarakaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ConventionalTags.Blocks.ORES)
                .addTag(Tags.Blocks.ORES)
                .addOptionalTag(NarakaBlockTags.AMETHYST_ORES)
                .addOptionalTag(NarakaBlockTags.NECTARIUM_ORES);
    }
}

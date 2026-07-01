package com.yummy.naraka.forge.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.ConventionalTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class NarakaForgeBiomeTagsProvider extends BiomeTagsProvider {
    public NarakaForgeBiomeTagsProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, NarakaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ConventionalTags.Biomes.IS_PLAINS)
                .addTag(Tags.Biomes.IS_PLAINS);
        tag(ConventionalTags.Biomes.IS_OVERWORLD)
                .addTag(Tags.Biomes.IS_COLD_OVERWORLD)
                .addTag(Tags.Biomes.IS_DENSE_OVERWORLD)
                .addTag(Tags.Biomes.IS_DRY_OVERWORLD)
                .addTag(Tags.Biomes.IS_HOT_OVERWORLD)
                .addTag(Tags.Biomes.IS_WET_OVERWORLD)
                .addTag(Tags.Biomes.IS_SPARSE_OVERWORLD);
    }
}

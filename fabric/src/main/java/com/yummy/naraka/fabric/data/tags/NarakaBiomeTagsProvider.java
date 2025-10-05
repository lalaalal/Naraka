package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.tags.NarakaBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class NarakaBiomeTagsProvider extends FabricTagProvider<Biome> {
    public NarakaBiomeTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.BIOME, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(NarakaBiomeTags.HEROBRINE_SANCTUARY_BIOMES)
                .forceAddTag(ConventionalTags.Biomes.IS_PLAINS)
                .add(Biomes.FOREST)
                .add(Biomes.BIRCH_FOREST);
    }
}

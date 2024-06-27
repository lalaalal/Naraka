package com.yummy.naraka.data.tags;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import com.yummy.naraka.tags.NarakaPlacementTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class NarakaPlacementTagsProvider extends TagsProvider<PlacedFeature> {
    public NarakaPlacementTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(packOutput, Registries.PLACED_FEATURE, provider, NarakaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(NarakaPlacementTags.NECTARIUM)
                .add(NarakaOrePlacements.NECTARIUM_ORE_SMALL_PLACED_KEY)
                .add(NarakaOrePlacements.NECTARIUM_ORE_LARGE_PLACED_KEY)
                .add(NarakaOrePlacements.NECTARIUM_ORE_BURIED_PLACED_KEY);
    }
}

package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import com.yummy.naraka.tags.NarakaPlacementTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.concurrent.CompletableFuture;

public class NarakaPlacementTagsProvider extends FabricTagsProvider<PlacedFeature> {
    public NarakaPlacementTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.PLACED_FEATURE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(NarakaPlacementTags.NECTARIUM)
                .add(NarakaOrePlacements.NECTARIUM_ORE_SMALL)
                .add(NarakaOrePlacements.NECTARIUM_ORE_LARGE)
                .add(NarakaOrePlacements.NECTARIUM_ORE_BURIED);
    }
}

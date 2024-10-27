package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import com.yummy.naraka.tags.NarakaPlacementTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.concurrent.CompletableFuture;

public class NarakaPlacementTagsProvider extends FabricTagProvider<PlacedFeature> {
    public NarakaPlacementTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.PLACED_FEATURE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(NarakaPlacementTags.NECTARIUM)
                .add(NarakaOrePlacements.NECTARIUM_ORE_SMALL_PLACED_KEY)
                .add(NarakaOrePlacements.NECTARIUM_ORE_LARGE_PLACED_KEY)
                .add(NarakaOrePlacements.NECTARIUM_ORE_BURIED_PLACED_KEY);
    }
}

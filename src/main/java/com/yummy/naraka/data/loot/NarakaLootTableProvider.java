package com.yummy.naraka.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NarakaLootTableProvider extends LootTableProvider {
    private static final List<SubProviderEntry> SUB_PROVIDER_ENTRIES = List.of(
            new SubProviderEntry(NarakaBlockLootSubProvider::new, LootContextParamSets.BLOCK),
            new SubProviderEntry(NarakaEntityLootSubProvider::new, LootContextParamSets.ENTITY)
    );

    public NarakaLootTableProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, Collections.emptySet(), SUB_PROVIDER_ENTRIES, provider);
    }
}

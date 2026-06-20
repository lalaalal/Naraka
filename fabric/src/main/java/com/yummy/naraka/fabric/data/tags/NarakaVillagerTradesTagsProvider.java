package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.world.item.trading.NarakaVillagerTrades;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.VillagerTradeTags;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.concurrent.CompletableFuture;

public class NarakaVillagerTradesTagsProvider extends FabricTagsProvider<VillagerTrade> {
    /**
     * Constructs a new {@link FabricTagsProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output               the {@link FabricPackOutput} instance
     * @param registryLookupFuture the backing registry for the tag type
     */
    public NarakaVillagerTradesTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.VILLAGER_TRADE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(VillagerTradeTags.WANDERING_TRADER_UNCOMMON)
                .add(NarakaVillagerTrades.WANDERING_TRADER_EMERALD_SANCTUARY_COMPASS)
                .add(NarakaVillagerTrades.WANDERING_TRADER_BEE_NEST_NECTARIUM_CORE);
    }
}

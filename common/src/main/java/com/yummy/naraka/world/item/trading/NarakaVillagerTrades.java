package com.yummy.naraka.world.item.trading;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.references.NarakaBlockItemIds;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.Optional;

public class NarakaVillagerTrades {
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_SANCTUARY_COMPASS = key("wandering_trader_emerald_sanctuary_compass");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_BEE_NEST_NECTARIUM_CORE = key("wandering_trader_bee_nest_nectarium_core");

    public static void bootstrap(final BootstrapContext<VillagerTrade> context) {
        context.register(WANDERING_TRADER_EMERALD_SANCTUARY_COMPASS,
                new VillagerTrade(
                        new TradeCost(Items.EMERALD, 10),
                        Optional.empty(),
                        new ItemStackTemplate(NarakaItems.SANCTUARY_COMPASS, 1, DataComponentPatch.EMPTY),
                        ContextIntProviders.exactly(1),
                        ContextIntProviders.exactly(1),
                        ContextFloatProviders.exactly(0.05f),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )
        );
        context.register(WANDERING_TRADER_BEE_NEST_NECTARIUM_CORE,
                new VillagerTrade(
                        new TradeCost(Items.BEE_NEST, 1),
                        Optional.empty(),
                        new ItemStackTemplate(BuiltInRegistries.ITEM.getOrThrow(NarakaBlockItemIds.NECTARIUM_CORE_BLOCK.item()), 1, DataComponentPatch.EMPTY),
                        ContextIntProviders.exactly(1),
                        ContextIntProviders.exactly(1),
                        ContextFloatProviders.exactly(0.05f),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )
        );
    }

    public static ResourceKey<VillagerTrade> key(String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, NarakaMod.identifier(name));
    }
}

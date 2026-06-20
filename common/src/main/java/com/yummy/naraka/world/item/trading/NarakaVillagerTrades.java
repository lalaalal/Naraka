package com.yummy.naraka.world.item.trading;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.List;
import java.util.Optional;

public class NarakaVillagerTrades {
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_SANCTUARY_COMPASS = key("wandering_trader_emerald_sanctuary_compass");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_BEE_NEST_NECTARIUM_CORE = key("wandering_trader_bee_nest_nectarium_core");

    public static void bootstrap(final BootstrapContext<VillagerTrade> context) {
        context.register(WANDERING_TRADER_EMERALD_SANCTUARY_COMPASS,
                new VillagerTrade(
                        new TradeCost(Items.EMERALD, 10), new ItemStackTemplate(NarakaItems.SANCTUARY_COMPASS.get(), 1), 1, 1, 0.05F, Optional.empty(), List.of()
                )
        );
        context.register(WANDERING_TRADER_BEE_NEST_NECTARIUM_CORE,
                new VillagerTrade(
                        new TradeCost(Items.BEE_NEST, 1), new ItemStackTemplate(NarakaBlocks.NECTARIUM_CORE_BLOCK.get().asItem(), 1), 1, 1, 0.05F, Optional.empty(), List.of()
                )
        );
    }

    public static ResourceKey<VillagerTrade> key(String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, NarakaMod.identifier(name));
    }
}

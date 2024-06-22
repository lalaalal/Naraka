package com.yummy.naraka.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.block.NarakaBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NarakaCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NarakaMod.MOD_ID);

    private static final DeferredHolder<CreativeModeTab, CreativeModeTab> NARAKA_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.naraka"))
            .icon(() -> NarakaItems.TEST_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(NarakaBlocks.NECTARIUM_ORE);
                output.accept(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE);
                output.accept(NarakaBlocks.NECTARIUM_BLOCK);

                output.accept(NarakaItems.NECTARIUM);

                output.accept(NarakaItems.PURIFIED_SOUL_METAL);
                output.accept(NarakaItems.PURIFIED_SOUL_SHARD);
                output.accept(NarakaItems.SOUL_INFUSED_AMETHYST);
                output.accept(NarakaItems.SOUL_INFUSED_COPPER);
                output.accept(NarakaItems.SOUL_INFUSED_DIAMOND);
                output.accept(NarakaItems.SOUL_INFUSED_EMERALD);
                output.accept(NarakaItems.SOUL_INFUSED_GOLD);
                output.accept(NarakaItems.SOUL_INFUSED_LAPIS);
                output.accept(NarakaItems.SOUL_INFUSED_NECTARIUM);
                output.accept(NarakaItems.SOUL_INFUSED_REDSTONE);

                output.accept(NarakaItems.SPEAR_ITEM);
                output.accept(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM);
                output.accept(NarakaItems.SPEAR_OF_LONGINUS_ITEM);

                output.accept(NarakaItems.GOD_BLOOD);
                output.accept(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE);
                output.accept(NarakaItems.PURIFIED_GEMS_UPGRADE_SMITHING_TEMPLATE);
            }).build());

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
//            event.accept(EXAMPLE_BLOCK_ITEM);
    }
}

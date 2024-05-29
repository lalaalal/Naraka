package com.yummy.naraka.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.block.NarakaBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NarakaMod.MOD_ID);

    private static final DeferredHolder<CreativeModeTab, CreativeModeTab> NARAKA_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.naraka")) //The language key for the title of your CreativeModeTab
            .icon(() -> NarakaItems.TEST_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(NarakaItems.TEST_ITEM);
                output.accept(NarakaBlocks.TRANSPARENT_BLOCK);
            }).build());

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}

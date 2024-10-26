package com.yummy.naraka.init;

import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Consumer;

public interface NarakaInitializer {
    RegistryFactory getRegistryFactory();

    void modifyCreativeModeTab(ResourceKey<CreativeModeTab> tabKey, Consumer<NarakaCreativeModTabs.TabEntries> entries);
}

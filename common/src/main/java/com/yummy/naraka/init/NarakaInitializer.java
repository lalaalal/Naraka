package com.yummy.naraka.init;

import com.yummy.naraka.Platform;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.world.NarakaBiomes;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;

public interface NarakaInitializer extends RegistryLoadedListener {
    Platform getPlatform();

    RegistryInitializer getRegistryInitializer();

    RegistryFactory getRegistryFactory();

    NarakaCreativeModTabs.CreativeModeTabModifier getCreativeModeTabModifier();

    NarakaBiomes.Modifier getBiomeModifier();
}

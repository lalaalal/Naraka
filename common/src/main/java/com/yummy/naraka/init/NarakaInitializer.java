package com.yummy.naraka.init;

import com.yummy.naraka.Platform;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.world.NarakaBiomes;
import com.yummy.naraka.world.item.NarakaCreativeModeTabs;

public interface NarakaInitializer extends RegistryLoadedListener {
    Platform getPlatform();

    RegistryInitializer getRegistryInitializer();

    RegistryFactory getRegistryFactory();

    NarakaCreativeModeTabs.CreativeModeTabModifier getCreativeModeTabModifier();

    NarakaBiomes.Modifier getBiomeModifier();
}

package com.yummy.naraka.init;

import com.yummy.naraka.Platform;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryLoadedListener;
import com.yummy.naraka.world.NarakaBiomes;

public interface NarakaInitializer extends RegistryLoadedListener {
    Platform getPlatform();

    RegistryInitializer getRegistryInitializer();

    RegistryFactory getRegistryFactory();

    NarakaBiomes.Modifier getBiomeModifier();
}

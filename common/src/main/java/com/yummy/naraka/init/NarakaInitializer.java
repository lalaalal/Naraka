package com.yummy.naraka.init;

import com.yummy.naraka.core.registries.RegistryLoadedListener;
import com.yummy.naraka.world.NarakaBiomes;

public interface NarakaInitializer extends RegistryLoadedListener {
    NarakaBiomes.Modifier getBiomeModifier();
}

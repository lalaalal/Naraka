package com.yummy.naraka.init;

import com.yummy.naraka.core.registries.RegistryFactory;

public interface NarakaInitializer {
    RegistryFactory getRegistryFactory();

    void registerCreativeModeTabs();
}

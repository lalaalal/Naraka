package com.yummy.naraka.core.registries;

import net.minecraft.core.Holder;

public interface NarakaRegisterProxy<T> {
    static <T> T register(NarakaRegisterProxy<T> proxy, String name, T value) {
        return proxy.register(name, value);
    }

    Holder<T> registerForHolder(String name, T value);

    default T register(String name, T value) {
        return registerForHolder(name, value).value();
    }
}

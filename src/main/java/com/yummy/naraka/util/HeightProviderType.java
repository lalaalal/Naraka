package com.yummy.naraka.util;

import net.minecraft.util.valueproviders.IntProvider;

@FunctionalInterface
public interface HeightProviderType {
    HeightProvider create(IntProvider sampler);
}

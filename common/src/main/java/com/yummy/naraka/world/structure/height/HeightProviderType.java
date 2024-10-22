package com.yummy.naraka.world.structure.height;

import net.minecraft.util.valueproviders.IntProvider;

@FunctionalInterface
public interface HeightProviderType {
    HeightProvider create(IntProvider sampler);
}

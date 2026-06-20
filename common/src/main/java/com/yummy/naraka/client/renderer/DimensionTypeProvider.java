package com.yummy.naraka.client.renderer;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface DimensionTypeProvider {
    void naraka$setDimensionType(ResourceKey<Level> key);

    ResourceKey<Level> naraka$getDimensionType();
}

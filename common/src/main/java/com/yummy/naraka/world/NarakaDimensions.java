package com.yummy.naraka.world;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class NarakaDimensions {
    public static final ResourceKey<Level> NARAKA = create("naraka");

    private static ResourceKey<Level> create(String name) {
        return ResourceKey.create(Registries.DIMENSION, NarakaMod.location(name));
    }
}

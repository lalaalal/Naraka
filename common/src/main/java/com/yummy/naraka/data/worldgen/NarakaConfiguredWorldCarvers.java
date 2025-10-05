package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;

public class NarakaConfiguredWorldCarvers {
    public static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> context) {

    }

    private static ResourceKey<ConfiguredWorldCarver<?>> create(String name) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, NarakaMod.location(name));
    }
}

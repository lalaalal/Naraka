package com.yummy.naraka.world.features;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;

public class NarakaFeatureTypes {
    private static void register(String name, MapCodec<? extends Feature> mapCodec) {
        RegistryProxy.register(Registries.FEATURE_TYPE, name, () -> mapCodec);
    }

    public static void initialize() {
        register("ore_pillar", OrePillarFeature.CODEC);
    }
}

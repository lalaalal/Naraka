package com.yummy.naraka.world.features;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.function.Supplier;

public class NarakaFeatures {
    public static final LazyHolder<Feature<?>, OrePillarFeature> ORE_PILLAR = register("ore_pillar", OrePillarFeature::new);

    private static <C extends FeatureConfiguration, F extends Feature<C>> LazyHolder<Feature<?>, F> register(String name, Supplier<F> feature) {
        return RegistryProxy.register(Registries.FEATURE, name, feature);
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.FEATURE)
                .onRegistrationFinished();
    }
}

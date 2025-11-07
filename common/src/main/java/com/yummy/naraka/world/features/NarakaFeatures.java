package com.yummy.naraka.world.features;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.function.Supplier;

public class NarakaFeatures {
    public static final HolderProxy<Feature<?>, OrePillarFeature> ORE_PILLAR = register("ore_pillar", OrePillarFeature::new);
    public static final HolderProxy<Feature<?>, NarakaPortalFeature> NARAKA_PORTAL = register("naraka_portal", NarakaPortalFeature::new);

    private static <C extends FeatureConfiguration, F extends Feature<C>> HolderProxy<Feature<?>, F> register(String name, Supplier<F> feature) {
        return RegistryProxy.register(Registries.FEATURE, name, feature);
    }

    public static void initialize() {

    }
}

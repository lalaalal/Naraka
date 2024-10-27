package com.yummy.naraka.world.rootplacer;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.RegistryInitializer;
import com.yummy.naraka.mixin.invoker.RootPlacerTypeInvoker;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;

public class NarakaRootPlacerTypes {
    public static final LazyHolder<RootPlacerType<?>, RootPlacerType<EbonyRootPlacer>> EBONY_ROOT = register("ebony_root", EbonyRootPlacer.CODEC);

    private static <P extends RootPlacer> LazyHolder<RootPlacerType<?>, RootPlacerType<P>> register(String name, MapCodec<P> codec) {
        return RegistryProxy.register(Registries.ROOT_PLACER_TYPE, name, () -> RootPlacerTypeInvoker.create(codec));
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.ROOT_PLACER_TYPE)
                .onRegistrationFinished();
    }
}

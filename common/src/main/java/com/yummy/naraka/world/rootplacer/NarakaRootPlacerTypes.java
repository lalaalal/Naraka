package com.yummy.naraka.world.rootplacer;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.mixin.invoker.RootPlacerTypeInvoker;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;

public class NarakaRootPlacerTypes {
    private static <P extends RootPlacer> HolderProxy<RootPlacerType<?>, RootPlacerType<P>> register(String name, MapCodec<P> codec) {
        return RegistryProxy.register(Registries.ROOT_PLACER_TYPE, name, () -> RootPlacerTypeInvoker.create(codec));
    }

    public static void initialize() {

    }
}

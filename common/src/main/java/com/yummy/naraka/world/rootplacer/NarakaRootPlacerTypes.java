package com.yummy.naraka.world.rootplacer;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.mixin.invoker.RootPlacerTypeInvoker;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;

public class NarakaRootPlacerTypes {
    private static final DeferredRegister<RootPlacerType<?>> ROOT_PLACER_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, Registries.ROOT_PLACER_TYPE);

    public static final RegistrySupplier<RootPlacerType<EbonyRootPlacer>> EBONY_ROOT = register("ebony_root", EbonyRootPlacer.CODEC);

    private static <P extends RootPlacer> RegistrySupplier<RootPlacerType<P>> register(String name, MapCodec<P> codec) {
        return ROOT_PLACER_TYPES.register(name, () -> RootPlacerTypeInvoker.create(codec));
    }

    public static void initialize() {
        ROOT_PLACER_TYPES.register();
    }
}

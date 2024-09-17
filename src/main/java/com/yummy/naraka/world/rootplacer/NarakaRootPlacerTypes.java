package com.yummy.naraka.world.rootplacer;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;

public class NarakaRootPlacerTypes {
    public static final RootPlacerType<EbonyRootPlacer> EBONY_ROOT = register("ebony_root", EbonyRootPlacer.CODEC);

    private static <P extends RootPlacer> RootPlacerType<P> register(String name, MapCodec<P> codec) {
        return Registry.register(BuiltInRegistries.ROOT_PLACER_TYPE, NarakaMod.location(name), new RootPlacerType<>(codec));
    }

    public static void initialize() {

    }
}

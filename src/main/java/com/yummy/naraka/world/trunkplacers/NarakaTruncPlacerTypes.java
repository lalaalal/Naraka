package com.yummy.naraka.world.trunkplacers;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public class NarakaTruncPlacerTypes {
    public static final TrunkPlacerType<EbonyTrunkPlacer> EBONY_TRUNK_PLACER = register("ebony_trunk_placer", EbonyTrunkPlacer.CODEC);

    private static <T extends TrunkPlacer> TrunkPlacerType<T> register(String name, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, NarakaMod.location(name), new TrunkPlacerType<>(codec));
    }

    public static void initialize() {

    }
}

package com.yummy.naraka.world.carver;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

import java.util.function.Supplier;

public class NarakaWorldCarvers {
    public static final HolderProxy<WorldCarver<?>, PillarCaveCarver> PILLAR_CAVE = register("pillar_cave", PillarCaveCarver::new);

    private static <C extends CarverConfiguration, T extends WorldCarver<C>> HolderProxy<WorldCarver<?>, T> register(String name, Supplier<T> carver) {
        return RegistryProxy.register(Registries.CARVER, name, carver);
    }

    public static void initialize() {

    }
}

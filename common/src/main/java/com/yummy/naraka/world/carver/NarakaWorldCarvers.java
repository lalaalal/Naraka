package com.yummy.naraka.world.carver;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryWriter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

import java.util.function.Supplier;

public class NarakaWorldCarvers {
    private static <C extends CarverConfiguration, T extends WorldCarver<C>> HolderProxy<WorldCarver<?>, T> register(String name, Supplier<T> carver) {
        return RegistryWriter.register(Registries.CARVER, name, carver);
    }

    public static void initialize() {

    }
}

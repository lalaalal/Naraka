package com.yummy.naraka.neoforge;

import com.yummy.naraka.util.Platform;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class NeoForgePlatform extends Platform {
    public static final NeoForgePlatform INSTANCE = new NeoForgePlatform();

    private NeoForgePlatform() {
        super(ModLoader.NEO_FORGE);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Path getConfigurationPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}

package com.yummy.naraka.neoforge;

import com.yummy.naraka.Platform;
import com.yummy.naraka.proxy.MethodProxy;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class NeoForgePlatform extends Platform {
    private static final NeoForgePlatform INSTANCE = new NeoForgePlatform();

    @SuppressWarnings("unused")
    @MethodProxy(Platform.class)
    public static Platform getInstance() {
        return INSTANCE;
    }

    private NeoForgePlatform() {
        super(ModLoader.NEO_FORGE);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Side getSide() {
        return switch (FMLLoader.getDist()) {
            case DEDICATED_SERVER -> Side.SERVER;
            case CLIENT -> Side.CLIENT;
        };
    }

    @Override
    public Path getConfigurationPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}

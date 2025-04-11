package com.yummy.naraka.fabric;

import com.yummy.naraka.Platform;
import com.yummy.naraka.proxy.MethodProxy;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public final class FabricPlatform extends Platform {
    private static final FabricPlatform INSTANCE = new FabricPlatform();

    @SuppressWarnings("unused")
    @MethodProxy(Platform.class)
    public static Platform getInstance() {
        return INSTANCE;
    }

    private FabricPlatform() {
        super(ModLoader.FABRIC);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Side getSide() {
        return switch (FabricLoader.getInstance().getEnvironmentType()) {
            case SERVER -> Side.SERVER;
            case CLIENT -> Side.CLIENT;
        };
    }

    @Override
    public Path getConfigurationPath() {
        return FabricLoader.getInstance().getConfigDir();
    }
}

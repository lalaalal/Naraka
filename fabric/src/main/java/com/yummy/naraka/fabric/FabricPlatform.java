package com.yummy.naraka.fabric;

import com.yummy.naraka.util.Platform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public final class FabricPlatform extends Platform {
    public static final FabricPlatform INSTANCE = new FabricPlatform();

    private FabricPlatform() {
        super(ModLoader.FABRIC);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path getConfigurationPath() {
        return FabricLoader.getInstance().getConfigDir();
    }
}

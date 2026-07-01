package com.yummy.naraka.forge;

import com.yummy.naraka.Platform;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class ForgePlatform extends Platform {
    private static final ForgePlatform INSTANCE = new ForgePlatform();

    @SuppressWarnings("unused")
    @MethodProxy(Platform.class)
    public static Platform getInstance() {
        return INSTANCE;
    }

    private ForgePlatform() {
        super(ModLoader.FORGE);
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

    @Override
    public boolean modExists(String id) {
        return false;
    }
}

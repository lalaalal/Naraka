package com.yummy.naraka.neoforge;

import com.yummy.naraka.Platform;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class NeoForgePlatform extends Platform {
    public NeoForgePlatform() {
        super(ModLoader.NEO_FORGE);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.isProduction();
    }

    @Override
    public Side getSide() {
        return switch (FMLEnvironment.getDist()) {
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
        return FMLLoader.getCurrent().getLoadingModList().getMods().stream()
                .anyMatch(modInfo -> modInfo.getModId().equals(id));
    }
}

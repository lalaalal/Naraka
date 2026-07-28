package com.yummy.naraka;

import java.nio.file.Path;
import java.util.ServiceLoader;

public abstract class Platform {
    public static final Platform INSTANCE = ServiceLoader.load(Platform.class, Platform.class.getClassLoader())
            .findFirst()
            .orElseThrow(() -> new NullPointerException("Failed to load service for " + Platform.class.getName()));

    private final ModLoader modLoader;

    public static Platform getInstance() {
        return INSTANCE;
    }

    protected Platform(ModLoader modLoader) {
        this.modLoader = modLoader;
    }

    public ModLoader getModLoader() {
        return modLoader;
    }

    public abstract Side getSide();

    public abstract boolean isDevelopmentEnvironment();

    public abstract Path getConfigurationPath();

    public abstract boolean modExists(String id);

    public enum Side {
        CLIENT, SERVER
    }

    public enum ModLoader {
        FABRIC, FORGE
    }
}

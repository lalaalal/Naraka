package com.yummy.naraka.util;

import com.yummy.naraka.init.NarakaInitializer;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public abstract class Platform {
    @Nullable
    private static Platform INSTANCE = null;

    private final ModLoader modLoader;

    public static Platform getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("Platform is not loaded");
        return INSTANCE;
    }

    public static void initialize(NarakaInitializer initializer) {
        if (INSTANCE != null)
            throw new IllegalStateException("Platform is already loaded");
        INSTANCE = initializer.getPlatform();
    }

    protected Platform(ModLoader modLoader) {
        this.modLoader = modLoader;
    }

    public ModLoader getModLoader() {
        return modLoader;
    }

    public abstract boolean isDevelopmentEnvironment();

    public abstract Path getConfigurationPath();

    public enum ModLoader {
        FABRIC, NEO_FORGE
    }
}

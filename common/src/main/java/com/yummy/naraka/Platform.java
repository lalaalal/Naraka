package com.yummy.naraka;

import com.yummy.naraka.invoker.MethodInvoker;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public abstract class Platform {
    @Nullable
    private static Platform instance = null;

    private final ModLoader modLoader;

    public static Platform getInstance() {
        if (instance == null)
            instance = MethodInvoker.of(Platform.class, "getInstance")
                    .invoke().result(Platform.class);
        return instance;
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

    public enum Side {
        CLIENT, SERVER
    }

    public enum ModLoader {
        FABRIC, NEO_FORGE
    }
}

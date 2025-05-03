package com.yummy.naraka.core.registries;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

public abstract class RegistryFactory {
    @Nullable
    private static RegistryFactory instance = null;

    private static RegistryFactory getInstance() {
        if (instance == null)
            instance = MethodInvoker.of(RegistryFactory.class, "getInstance")
                    .invoke().result(RegistryFactory.class);
        return instance;
    }

    public static void initialize() {
        getInstance();
    }

    public static <T> Registry<T> create(ResourceKey<Registry<T>> key) {
        return getInstance().createSimple(key);
    }

    protected abstract <T> Registry<T> createSimple(ResourceKey<Registry<T>> key);
}
package com.yummy.naraka.init;

import com.yummy.naraka.client.gui.components.ProgressOverlayExtension;
import com.yummy.naraka.world.overlay.ProgressOverlayExtensionType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class ProgressOverlayExtensionFactoryRegistry {
    private static final Map<ProgressOverlayExtensionType<?>, Supplier<? extends ProgressOverlayExtension<?>>> FACTORIES = new HashMap<>();

    public static <T> void register(Supplier<ProgressOverlayExtensionType<T>> type, Supplier<ProgressOverlayExtension<T>> factory) {
        FACTORIES.put(type.get(), factory);
    }

    @SuppressWarnings("unchecked")
    public static <T> ProgressOverlayExtension<T> create(ProgressOverlayExtensionType<T> type) {
        if (FACTORIES.containsKey(type))
            return (ProgressOverlayExtension<T>) FACTORIES.get(type).get();
        throw new IllegalStateException("No ProgressOverlayExtension registered for type " + type);
    }
}

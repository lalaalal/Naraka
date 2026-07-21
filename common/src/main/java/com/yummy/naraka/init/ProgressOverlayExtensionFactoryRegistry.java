package com.yummy.naraka.init;

import com.yummy.naraka.client.gui.components.ProgressOverlayExtension;
import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.world.overlay.ProgressOverlayExtensionType;

import java.util.HashMap;
import java.util.Map;

public abstract class ProgressOverlayExtensionFactoryRegistry {
    private static final Map<ProgressOverlayExtensionType<?>, ValueGetter<? extends ProgressOverlayExtension<?>>> FACTORIES = new HashMap<>();

    public static <T> void register(ValueGetter<ProgressOverlayExtensionType<T>> type, ValueGetter<ProgressOverlayExtension<T>> factory) {
        FACTORIES.put(type.getConcreteValue(), factory);
    }

    @SuppressWarnings("unchecked")
    public static <T> ProgressOverlayExtension<T> create(ProgressOverlayExtensionType<T> type) {
        if (FACTORIES.containsKey(type))
            return (ProgressOverlayExtension<T>) FACTORIES.get(type).getConcreteValue();
        throw new IllegalStateException("No ProgressOverlayExtension registered for type " + type);
    }
}

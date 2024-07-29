package com.yummy.naraka.world.item.component;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class NarakaDataComponents {
    public static final DataComponentType<SanctuaryTracker> SANCTUARY_TRACKER = register(
            "sanctuary_tracker",
            DataComponentType.<SanctuaryTracker>builder()
                    .persistent(SanctuaryTracker.CODEC)
                    .networkSynchronized(SanctuaryTracker.STREAM_CODEC)
                    .build()
    );

    private static <T> DataComponentType<T> register(String name, DataComponentType<T> componentType) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, NarakaMod.location(name), componentType);
    }

    public static void initialize() {

    }
}

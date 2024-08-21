package com.yummy.naraka.world.item.component;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class NarakaDataComponentTypes {
    public static final DataComponentType<SanctuaryTracker> SANCTUARY_TRACKER = register(
            "sanctuary_tracker",
            DataComponentType.<SanctuaryTracker>builder()
                    .persistent(SanctuaryTracker.CODEC)
                    .networkSynchronized(SanctuaryTracker.STREAM_CODEC)
                    .build()
    );

    public static final DataComponentType<Reinforcement> REINFORCEMENT = register(
            "reinforcement",
            DataComponentType.<Reinforcement>builder()
                    .persistent(Reinforcement.CODEC)
                    .networkSynchronized(Reinforcement.STREAM_CODEC)
                    .build()
    );

    private static <T> DataComponentType<T> register(String name, DataComponentType<T> componentType) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, NarakaMod.location(name), componentType);
    }

    public static void initialize() {

    }
}

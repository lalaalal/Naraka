package com.yummy.naraka.world.item.component;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.function.UnaryOperator;

public class NarakaDataComponentTypes {
    public static final HolderProxy<DataComponentType<?>, DataComponentType<SanctuaryTracker>> SANCTUARY_TRACKER = register(
            "sanctuary_tracker",
            builder -> builder.persistent(SanctuaryTracker.CODEC)
                    .networkSynchronized(SanctuaryTracker.STREAM_CODEC)
    );

    public static final HolderProxy<DataComponentType<?>, DataComponentType<Reinforcement>> REINFORCEMENT = register(
            "reinforcement",
            builder -> builder.persistent(Reinforcement.CODEC)
                    .networkSynchronized(Reinforcement.STREAM_CODEC)
    );

    public static final HolderProxy<DataComponentType<?>, DataComponentType<Boolean>> BLESSED = register(
            "blessed",
            builder -> builder.persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final HolderProxy<DataComponentType<?>, DataComponentType<SoulType>> SOUL = register(
            "soul",
            builder -> builder.persistent(SoulType.CODEC)
                    .networkSynchronized(SoulType.STREAM_CODEC)
    );

    private static <T> HolderProxy<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return RegistryProxy.register(Registries.DATA_COMPONENT_TYPE, name, () -> builder.apply(DataComponentType.builder()).build());
    }

    public static void initialize() {

    }
}

package com.yummy.naraka.world.item.component;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.function.UnaryOperator;

public class NarakaDataComponentTypes {
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, Registries.DATA_COMPONENT_TYPE);

    public static final RegistrySupplier<DataComponentType<SanctuaryTracker>> SANCTUARY_TRACKER = register(
            "sanctuary_tracker",
            builder -> builder.persistent(SanctuaryTracker.CODEC)
                    .networkSynchronized(SanctuaryTracker.STREAM_CODEC)
    );

    public static final RegistrySupplier<DataComponentType<Reinforcement>> REINFORCEMENT = register(
            "reinforcement",
            builder -> builder.persistent(Reinforcement.CODEC)
                    .networkSynchronized(Reinforcement.STREAM_CODEC)
    );

    public static final RegistrySupplier<DataComponentType<Boolean>> BLESSED = register(
            "blessed",
            builder -> builder.persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final RegistrySupplier<DataComponentType<SoulType>> SOUL = register(
            "soul",
            builder -> builder.persistent(SoulType.CODEC)
                    .networkSynchronized(SoulType.STREAM_CODEC)
    );

    private static <T> RegistrySupplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA_COMPONENT_TYPES.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }

    public static void initialize() {
        DATA_COMPONENT_TYPES.register();
    }
}

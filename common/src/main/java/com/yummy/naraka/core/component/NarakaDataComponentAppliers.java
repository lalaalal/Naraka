package com.yummy.naraka.core.component;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;

public class NarakaDataComponentAppliers {
    public static final HolderProxy<DataComponentApplier.Type<?>, DataComponentApplier.Type<DataComponentApplier.Empty>> EMPTY = register("empty", Codec.unit(DataComponentApplier.Empty::getInstance));

    public static final HolderProxy<DataComponentApplier.Type<?>, DataComponentApplier.Type<BlessApplier>> BLESS = register("bless", BlessApplier.CODEC);

    private static <T extends DataComponentApplier> HolderProxy<DataComponentApplier.Type<?>, DataComponentApplier.Type<T>> register(String name, Codec<T> codec) {
        return RegistryProxy.register(NarakaRegistries.Keys.DATA_COMPONENT_APPLIER, name, () -> new DataComponentApplier.Type<>(codec));
    }

    public static void initialize() {

    }
}

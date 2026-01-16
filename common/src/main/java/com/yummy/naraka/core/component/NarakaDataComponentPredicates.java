package com.yummy.naraka.core.component;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.Registries;

public class NarakaDataComponentPredicates {
    public static final HolderProxy<DataComponentPredicate.Type<?>, DataComponentPredicate.Type<BlessedPredicate>> BLESSED = register(
            "blessed", BlessedPredicate.CODEC
    );

    public static final HolderProxy<DataComponentPredicate.Type<?>, DataComponentPredicate.Type<AnyPredicate>> ANY = register(
            "any", AnyPredicate.CODEC
    );

    private static <T extends DataComponentPredicate> HolderProxy<DataComponentPredicate.Type<?>, DataComponentPredicate.Type<T>> register(String name, Codec<T> codec) {
        return RegistryProxy.register(Registries.DATA_COMPONENT_PREDICATE_TYPE, name, () -> new DataComponentPredicate.ConcreteType<>(codec));
    }

    public static void initialize() {

    }
}

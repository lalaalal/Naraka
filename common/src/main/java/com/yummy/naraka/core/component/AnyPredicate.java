package com.yummy.naraka.core.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.predicates.DataComponentPredicate;

public final class AnyPredicate implements DataComponentPredicate {
    public static final Codec<AnyPredicate> CODEC = Codec.unit(AnyPredicate::new);
    public static final AnyPredicate INSTANCE = new AnyPredicate();

    @Override
    public boolean matches(DataComponentGetter componentGetter) {
        return true;
    }
}

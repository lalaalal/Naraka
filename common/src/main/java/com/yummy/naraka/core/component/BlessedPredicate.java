package com.yummy.naraka.core.component;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

public record BlessedPredicate(boolean value) implements SingleComponentItemPredicate<Boolean>, ComponentItemApply {
    public static final Codec<BlessedPredicate> CODEC = Codec.BOOL.xmap(BlessedPredicate::new, BlessedPredicate::value);

    public static final BlessedPredicate BLESSED = new BlessedPredicate(true);

    @Override
    public DataComponentType<Boolean> componentType() {
        return NarakaDataComponentTypes.BLESSED.get();
    }

    @Override
    public boolean matches(Boolean value) {
        return this.value == value;
    }

    @Override
    public void apply(ItemStack itemStack) {
        itemStack.set(componentType(), value);
    }
}

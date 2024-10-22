package com.yummy.naraka.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class PredicateSlot extends Slot {
    private final Predicate<ItemStack> placePredicate;

    public PredicateSlot(Container pContainer, Predicate<ItemStack> placePredicate, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
        this.placePredicate = placePredicate;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return placePredicate.test(itemStack);
    }
}

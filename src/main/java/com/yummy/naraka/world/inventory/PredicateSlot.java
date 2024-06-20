package com.yummy.naraka.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class PredicateSlot extends Slot {
    private final Predicate<ItemStack> placePredicate;

    public static Builder builder(Container container) {
        return new Builder(container);
    }

    public PredicateSlot(Container pContainer, Predicate<ItemStack> placePredicate, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
        this.placePredicate = placePredicate;
    }

    public static class Builder {
        private final Container container;
        private int slot, x, y;
        private Predicate<ItemStack> predicate;

        private Builder(Container container) {
            this.container = container;
        }

        public Builder at(int slot) {
            this.slot = slot;
            return this;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder with(Predicate<ItemStack> predicate) {
            this.predicate = predicate;
            return this;
        }

        public PredicateSlot build() {
            return new PredicateSlot(container, predicate, slot, x, y);
        }
    }
}

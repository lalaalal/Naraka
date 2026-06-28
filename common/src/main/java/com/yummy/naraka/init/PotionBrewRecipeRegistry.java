package com.yummy.naraka.init;


import com.yummy.naraka.mixin.invoker.PotionBrewingInvoker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;

import java.util.function.Consumer;

public abstract class PotionBrewRecipeRegistry {
    private static final Builder BUILDER = new Builder();

    public static void register(Consumer<Builder> consumer) {
        consumer.accept(BUILDER);
    }

    public static final class Builder {
        private Builder() {

        }

        public void addMix(Potion potionEntry, Item potionIngredient, Potion potionResult) {
            PotionBrewingInvoker.addMix(potionEntry, potionIngredient, potionResult);
        }

        public void addContainer(Item container) {
            PotionBrewingInvoker.addContainer(container);
        }

        public void addContainerRecipe(Item from, Item ingredient, Item to) {
            PotionBrewingInvoker.addContainerRecipe(from, ingredient, to);
        }
    }
}

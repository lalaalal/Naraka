package com.yummy.naraka.mixin.invoker;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface PotionBrewingInvoker {
    @Invoker("addMix")
    static void addMix(Potion potionEntry, Item potionIngredient, Potion potionResult) {
        throw new AssertionError();
    }

    @Invoker("addContainer")
    static void addContainer(Item container) {
        throw new AssertionError();
    }

    @Invoker("addContainerRecipe")
    static void addContainerRecipe(Item from, Item ingredient, Item to) {
        throw new AssertionError();
    }
}

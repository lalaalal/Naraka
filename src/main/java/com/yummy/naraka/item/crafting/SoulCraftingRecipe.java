package com.yummy.naraka.item.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class SoulCraftingRecipe extends SingleItemRecipe {
    public static Serializer serializer() {
        return new Serializer();
    }

    public SoulCraftingRecipe(String pGroup, Ingredient pIngredient, ItemStack pResult) {
        super(NarakaRecipeTypes.SOUL_CRAFTING.get(), NarakaRecipeSerializers.SOUL_CRAFTING_RECIPE.get(), pGroup, pIngredient, pResult);
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return ingredient.test(input.item());
    }

    public static class Serializer extends SingleItemRecipe.Serializer<SoulCraftingRecipe> {
        protected Serializer() {
            super(SoulCraftingRecipe::new);
        }
    }
}

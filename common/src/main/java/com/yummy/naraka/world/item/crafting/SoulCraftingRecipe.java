package com.yummy.naraka.world.item.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class SoulCraftingRecipe extends SingleItemRecipe {
    public static final Serializer SERIALIZER = new Serializer();

    public SoulCraftingRecipe(String group, Ingredient ingredient, ItemStack result) {
        super(group, ingredient, result);
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return NarakaRecipeTypes.SOUL_CRAFTING.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.FURNACE_MISC;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return input().test(input.item());
    }

    public static class Serializer extends SingleItemRecipe.Serializer<SoulCraftingRecipe> {
        protected Serializer() {
            super(SoulCraftingRecipe::new);
        }
    }
}

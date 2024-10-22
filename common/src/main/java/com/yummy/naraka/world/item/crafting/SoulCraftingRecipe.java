package com.yummy.naraka.world.item.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class SoulCraftingRecipe extends SingleItemRecipe {
    public static final Serializer SERIALIZER = new Serializer();

    public SoulCraftingRecipe(String group, Ingredient ingredient, ItemStack result) {
        super(NarakaRecipeTypes.SOUL_CRAFTING.get(), NarakaRecipeSerializers.SOUL_CRAFTING_RECIPE.get(), group, ingredient, result);
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

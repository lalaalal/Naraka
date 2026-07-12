package com.yummy.naraka.jei;

import com.yummy.naraka.world.item.crafting.ComponentPredicateRecipe;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public class ComponentPredicateCraftingExtension implements ICraftingCategoryExtension<ComponentPredicateRecipe> {
    @Override
    public List<SlotDisplay> getIngredients(RecipeHolder<ComponentPredicateRecipe> recipeHolder) {
        return recipeHolder.value().getRecipeDisplay().ingredients();
    }

    @Override
    public int getWidth(RecipeHolder<ComponentPredicateRecipe> recipeHolder) {
        return 3;
    }

    @Override
    public int getHeight(RecipeHolder<ComponentPredicateRecipe> recipeHolder) {
        return 3;
    }
}

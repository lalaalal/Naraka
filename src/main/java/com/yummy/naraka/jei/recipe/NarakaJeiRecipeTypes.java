package com.yummy.naraka.jei.recipe;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.crafting.SoulCraftingRecipe;
import mezz.jei.api.recipe.RecipeType;

public interface NarakaJeiRecipeTypes {
    RecipeType<SoulCraftingRecipe> SOUL_CRAFTING = create("soul_crafting", SoulCraftingRecipe.class);

    static <T> RecipeType<T> create(String name, Class<? extends T> type) {
        return RecipeType.create(NarakaMod.MOD_ID, name, type);
    }
}

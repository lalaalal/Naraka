package com.yummy.naraka.jei.recipe;

import com.yummy.naraka.world.item.crafting.NarakaRecipeTypes;
import com.yummy.naraka.world.item.crafting.SoulCraftingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

public class NarakaRecipes {
    public static List<SoulCraftingRecipe> soulCrafting() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return List.of();
        RecipeManager recipeManager = level.getRecipeManager();
        return recipeManager.getAllRecipesFor(NarakaRecipeTypes.SOUL_CRAFTING)
                .stream()
                .map(RecipeHolder::value)
                .toList();
    }
}

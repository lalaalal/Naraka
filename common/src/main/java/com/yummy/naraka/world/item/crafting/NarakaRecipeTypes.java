package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

public class NarakaRecipeTypes {
    public static final RecipeType<SoulCraftingRecipe> SOUL_CRAFTING = register("soul_crafting");

    private static <I extends RecipeInput, T extends Recipe<I>> RecipeType<T> register(String name) {
        return Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                NarakaMod.location(name),
                new RecipeType<>() {
                    @Override
                    public String toString() {
                        return name;
                    }
                }
        );
    }

    public static void initialize() {
    }
}

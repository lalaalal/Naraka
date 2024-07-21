package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class NarakaRecipeSerializers {
    public static final RecipeSerializer<SoulCraftingRecipe> SOUL_CRAFTING_RECIPE = register(
            "soul_crafting", SoulCraftingRecipe.SERIALIZER
    );

    private static <I extends RecipeInput, T extends Recipe<I>> RecipeSerializer<T> register(String name, RecipeSerializer<T> serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, NarakaMod.location(name), serializer);
    }

    public static void initialize() {
    }
}

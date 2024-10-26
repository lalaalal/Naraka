package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.RegistryInitializer;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class NarakaRecipeSerializers {
    public static final LazyHolder<RecipeSerializer<?>, RecipeSerializer<SoulCraftingRecipe>> SOUL_CRAFTING_RECIPE = register(
            "soul_crafting", SoulCraftingRecipe.SERIALIZER
    );

    private static <I extends RecipeInput, T extends Recipe<I>> LazyHolder<RecipeSerializer<?>, RecipeSerializer<T>> register(String name, RecipeSerializer<T> serializer) {
        return RegistryProxy.register(Registries.RECIPE_SERIALIZER, name, () -> serializer);
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.RECIPE_SERIALIZER)
                .onRegistrationFinished();
    }
}

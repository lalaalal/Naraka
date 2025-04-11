package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

public class NarakaRecipeTypes {
    public static final HolderProxy<RecipeType<?>, RecipeType<SoulCraftingRecipe>> SOUL_CRAFTING = register("soul_crafting");

    private static <I extends RecipeInput, T extends Recipe<I>> HolderProxy<RecipeType<?>, RecipeType<T>> register(String name) {
        return RegistryProxy.register(Registries.RECIPE_TYPE, name, () -> new RecipeType<>() {
                    @Override
                    public String toString() {
                        return name;
                    }
                }
        );
    }

    public static void initialize() {
        RegistryProxyProvider.get(Registries.RECIPE_TYPE)
                .onRegistrationFinished();
    }
}

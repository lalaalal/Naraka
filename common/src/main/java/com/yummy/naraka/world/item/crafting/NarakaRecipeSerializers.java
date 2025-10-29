package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

public class NarakaRecipeSerializers {
    public static final HolderProxy<RecipeSerializer<?>, RecipeSerializer<ComponentPredicateRecipe>> COMPONENT_PREDICATE_RECIPE = register("component_predicate_recipe", ComponentPredicateRecipe.Serializer::new);

    private static <I extends RecipeInput, T extends Recipe<I>> HolderProxy<RecipeSerializer<?>, RecipeSerializer<T>> register(String name, Supplier<RecipeSerializer<T>> serializer) {
        return RegistryProxy.register(Registries.RECIPE_SERIALIZER, name, serializer);
    }

    public static void initialize() {

    }
}

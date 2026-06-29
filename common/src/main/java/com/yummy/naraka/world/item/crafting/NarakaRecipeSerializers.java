package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryWriter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

public class NarakaRecipeSerializers {
    private static <C extends Container, T extends Recipe<C>> HolderProxy<RecipeSerializer<?>, RecipeSerializer<T>> register(String name, Supplier<RecipeSerializer<T>> serializer) {
        return RegistryWriter.register(Registries.RECIPE_SERIALIZER, name, serializer);
    }

    public static void initialize() {

    }
}

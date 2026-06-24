package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class NarakaRecipeTypes {
    private static <C extends Container, T extends Recipe<C>> HolderProxy<RecipeType<?>, RecipeType<T>> register(String name) {
        return RegistryProxy.register(Registries.RECIPE_TYPE, name, () -> new RecipeType<>() {
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

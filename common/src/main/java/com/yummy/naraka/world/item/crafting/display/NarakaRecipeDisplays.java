package com.yummy.naraka.world.item.crafting.display;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

public class NarakaRecipeDisplays {
    public static final HolderProxy<RecipeDisplay.Type<?>, RecipeDisplay.Type<ComponentPredicateRecipeDisplay>> COMPONENT_PREDICATE = register(
            "component_predicate", ComponentPredicateRecipeDisplay.TYPE
    );

    private static <T extends RecipeDisplay> HolderProxy<RecipeDisplay.Type<?>, RecipeDisplay.Type<T>> register(String name, RecipeDisplay.Type<T> value) {
        return RegistryProxy.register(Registries.RECIPE_DISPLAY, name, () -> value);
    }

    public static void initialize() {

    }
}

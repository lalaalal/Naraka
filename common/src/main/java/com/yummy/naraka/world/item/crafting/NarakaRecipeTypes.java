package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.NarakaMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

public class NarakaRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, Registries.RECIPE_TYPE);

    public static final RegistrySupplier<RecipeType<SoulCraftingRecipe>> SOUL_CRAFTING = register("soul_crafting");

    private static <I extends RecipeInput, T extends Recipe<I>> RegistrySupplier<RecipeType<T>> register(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
                    @Override
                    public String toString() {
                        return name;
                    }
                }
        );
    }

    public static void initialize() {
        RECIPE_TYPES.register();
    }
}

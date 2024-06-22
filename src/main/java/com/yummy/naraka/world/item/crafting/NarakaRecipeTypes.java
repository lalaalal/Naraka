package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, NarakaMod.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<SoulCraftingRecipe>> SOUL_CRAFTING = register("soul_crafting");

    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(String identifier) {
        return RECIPE_TYPES.register(
                identifier,
                () -> new RecipeType<>() {
                    @Override
                    public String toString() {
                        return identifier;
                    }
                }
        );
    }

    public static void register(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }
}

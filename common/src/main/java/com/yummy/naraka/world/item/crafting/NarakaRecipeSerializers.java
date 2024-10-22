package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.NarakaMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class NarakaRecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(NarakaMod.MOD_ID, Registries.RECIPE_SERIALIZER);

    public static final RegistrySupplier<RecipeSerializer<SoulCraftingRecipe>> SOUL_CRAFTING_RECIPE = register(
            "soul_crafting", SoulCraftingRecipe.SERIALIZER
    );

    private static <I extends RecipeInput, T extends Recipe<I>> RegistrySupplier<RecipeSerializer<T>> register(String name, RecipeSerializer<T> serializer) {
        return RECIPE_SERIALIZERS.register(name, () -> serializer);
    }

    public static void initialize() {
        RECIPE_SERIALIZERS.register();
    }
}

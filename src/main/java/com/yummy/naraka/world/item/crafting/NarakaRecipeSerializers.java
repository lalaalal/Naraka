package com.yummy.naraka.world.item.crafting;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaRecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, NarakaMod.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SoulCraftingRecipe>> SOUL_CRAFTING_RECIPE = RECIPE_SERIALIZERS.register(
            "soul_crafting", SoulCraftingRecipe::serializer
    );

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
}

package com.yummy.naraka.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ComponentPredicateRecipe implements CraftingRecipe {
    private final ItemStack result;
    private final String group;
    private final CraftingBookCategory category;
    private final boolean showNotification;
    private final List<ComponentPredicateIngredient> predicateIngredients;
    private final List<Ingredient> ingredients;
    @Nullable
    private PlacementInfo placementInfo;

    public ComponentPredicateRecipe(ItemStack result, String group, CraftingBookCategory category, boolean showNotification, List<ComponentPredicateIngredient> predicateIngredients) {
        this.result = result;
        this.group = group;
        this.category = category;
        this.showNotification = showNotification;
        this.predicateIngredients = predicateIngredients;
        this.ingredients = predicateIngredients.stream().map(ComponentPredicateIngredient::ingredient).toList();
    }

    public List<Ingredient> ingredients() {
        return ingredients;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        for (ComponentPredicateIngredient ingredient : predicateIngredients) {
            if (!ingredient.test(input))
                return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return NarakaRecipeSerializers.COMPONENT_PREDICATE_RECIPE.get();
    }

    @Override
    public boolean showNotification() {
        return showNotification;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (placementInfo == null)
            return placementInfo = PlacementInfo.create(ingredients);
        return placementInfo;
    }

    @Override
    public CraftingBookCategory category() {
        return category;
    }

    @Override
    public String group() {
        return group;
    }

    public static class Serializer implements RecipeSerializer<ComponentPredicateRecipe> {
        private static final MapCodec<ComponentPredicateRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.STRING.fieldOf("group").forGetter(ComponentPredicateRecipe::group),
                        CraftingBookCategory.CODEC.fieldOf("category").forGetter(ComponentPredicateRecipe::category),
                        Codec.BOOL.fieldOf("show_notification").forGetter(ComponentPredicateRecipe::showNotification),
                        ComponentPredicateIngredient.CODEC.listOf(0, 9).fieldOf("ingredients").forGetter(recipe -> recipe.predicateIngredients)
                ).apply(instance, ComponentPredicateRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, ComponentPredicateRecipe> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC,
                recipe -> recipe.result,
                ByteBufCodecs.STRING_UTF8,
                ComponentPredicateRecipe::group,
                CraftingBookCategory.STREAM_CODEC,
                ComponentPredicateRecipe::category,
                ByteBufCodecs.BOOL,
                ComponentPredicateRecipe::showNotification,
                ByteBufCodecs.collection(ArrayList::new, ComponentPredicateIngredient.STREAM_CODEC),
                recipe -> recipe.predicateIngredients,
                ComponentPredicateRecipe::new
        );

        @Override
        public MapCodec<ComponentPredicateRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ComponentPredicateRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

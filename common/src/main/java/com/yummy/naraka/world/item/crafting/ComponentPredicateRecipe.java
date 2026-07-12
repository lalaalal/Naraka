package com.yummy.naraka.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.world.item.crafting.display.ComponentPredicateRecipeDisplay;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ComponentPredicateRecipe implements CraftingRecipe {
    public static final MapCodec<ComponentPredicateRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                    Codec.STRING.fieldOf("group").forGetter(ComponentPredicateRecipe::group),
                    CraftingBookCategory.CODEC.fieldOf("category").forGetter(ComponentPredicateRecipe::category),
                    Codec.BOOL.fieldOf("show_notification").forGetter(ComponentPredicateRecipe::showNotification),
                    ComponentPredicateIngredient.CODEC.listOf(0, 9).fieldOf("ingredients").forGetter(recipe -> recipe.predicateIngredients)
            ).apply(instance, ComponentPredicateRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentPredicateRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC,
            recipe -> recipe.result,
            ByteBufCodecs.STRING_UTF8,
            ComponentPredicateRecipe::group,
            CraftingBookCategory.STREAM_CODEC,
            ComponentPredicateRecipe::category,
            ByteBufCodecs.BOOL,
            ComponentPredicateRecipe::showNotification,
            ComponentPredicateIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
            recipe -> recipe.predicateIngredients,
            ComponentPredicateRecipe::new
    );

    private final ItemStackTemplate result;
    private final String group;
    private final CraftingBookCategory category;
    private final boolean showNotification;
    private final List<ComponentPredicateIngredient> predicateIngredients;
    private final List<Ingredient> ingredients;
    @Nullable
    private PlacementInfo placementInfo;
    private final ComponentPredicateRecipeDisplay display;

    public ComponentPredicateRecipe(ItemStackTemplate result, String group, CraftingBookCategory category, boolean showNotification, List<ComponentPredicateIngredient> predicateIngredients) {
        this.result = result;
        this.group = group;
        this.category = category;
        this.showNotification = showNotification;
        this.predicateIngredients = predicateIngredients;
        this.ingredients = predicateIngredients.stream()
                .map(ComponentPredicateIngredient::ingredient)
                .map(Ingredient::of)
                .toList();
        this.display = new ComponentPredicateRecipeDisplay(
                predicateIngredients.stream()
                        .map(ComponentPredicateIngredient::display)
                        .toList(),
                new SlotDisplay.ItemStackSlotDisplay(result)
        );
    }

    public List<Ingredient> ingredients() {
        return ingredients;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() != 3 || input.height() != 3)
            return false;
        for (ComponentPredicateIngredient ingredient : predicateIngredients) {
            if (!ingredient.test(input))
                return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return result.create();
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

    @Override
    public List<RecipeDisplay> display() {
        return List.of(display);
    }

    public ComponentPredicateRecipeDisplay getRecipeDisplay() {
        return display;
    }
}

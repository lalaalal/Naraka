package com.yummy.naraka.world.item.crafting;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.*;

public class ComponentPredicateRecipeBuilder implements RecipeBuilder {
    private final ItemStack result;
    @Nullable
    private String group;
    private final RecipeCategory category;
    private boolean showNotification;
    private final List<ComponentPredicateIngredient> predicateIngredients = new ArrayList<>();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public static ComponentPredicateRecipeBuilder predicate(RecipeCategory category, Holder<Item> result) {
        return new ComponentPredicateRecipeBuilder(category, result.value().getDefaultInstance());
    }

    public ComponentPredicateRecipeBuilder(RecipeCategory category, ItemStack result) {
        this.result = result;
        this.category = category;
    }

    public ComponentPredicateRecipeBuilder requires(int row, int column, ItemLike item, DataComponentPredicate predicate) {
        ComponentPredicateIngredient ingredient = new ComponentPredicateIngredient(
                row, column,
                Ingredient.of(item),
                predicate
        );
        predicateIngredients.add(ingredient);
        return this;
    }

    public ComponentPredicateRecipeBuilder requires(int row, int column, TagKey<Item> tag, DataComponentPredicate predicate) {
        predicateIngredients.add(new ComponentPredicateIngredient(row, column, Ingredient.of(tag), predicate));
        return this;
    }

    public ComponentPredicateRecipeBuilder showNotification() {
        this.showNotification = true;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        Advancement.Builder builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        ComponentPredicateRecipe recipe = new ComponentPredicateRecipe(result, Objects.requireNonNullElse(group, ""), RecipeBuilder.determineBookCategory(category), showNotification, predicateIngredients);
        output.accept(id, recipe, builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
}

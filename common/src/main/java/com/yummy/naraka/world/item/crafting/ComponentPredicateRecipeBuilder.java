package com.yummy.naraka.world.item.crafting;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.*;

public class ComponentPredicateRecipeBuilder implements RecipeBuilder {
    private final HolderGetter<Item> items;
    private final ItemStack result;
    @Nullable
    private String group;
    private final RecipeCategory category;
    private boolean showNotification;
    private final List<ComponentPredicateIngredient> predicateIngredients = new ArrayList<>();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public static ComponentPredicateRecipeBuilder predicate(HolderGetter<Item> items, RecipeCategory category, Holder<Item> result) {
        return new ComponentPredicateRecipeBuilder(items, category, result.value().getDefaultInstance());
    }

    public ComponentPredicateRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemStack result) {
        this.items = items;
        this.result = result;
        this.category = category;
    }

    public <T extends DataComponentPredicate> ComponentPredicateRecipeBuilder requires(int row, int column, ItemLike item, DataComponentPredicate.Type<T> type, T predicate) {
        ComponentPredicateIngredient ingredient = new ComponentPredicateIngredient(
                row, column,
                HolderSet.direct(BuiltInRegistries.ITEM::wrapAsHolder, item.asItem()),
                new DataComponentPredicate.Single<>(type, predicate)
        );
        predicateIngredients.add(ingredient);
        return this;
    }

    public <T extends DataComponentPredicate> ComponentPredicateRecipeBuilder requires(int row, int column, TagKey<Item> tag, DataComponentPredicate.Type<T> type, T predicate) {
        predicateIngredients.add(new ComponentPredicateIngredient(row, column, items.getOrThrow(tag), new DataComponentPredicate.Single<>(type, predicate)));
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
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> resourceKey) {
        Advancement.Builder builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        ComponentPredicateRecipe recipe = new ComponentPredicateRecipe(result, Objects.requireNonNullElse(group, ""), RecipeBuilder.determineBookCategory(category), showNotification, predicateIngredients);
        output.accept(resourceKey, recipe, builder.build(resourceKey.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
}

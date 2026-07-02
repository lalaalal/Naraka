package com.yummy.naraka.world.item.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.yummy.naraka.NarakaMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class NbtPredicateRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item resultItem;
    private final ResourceLocation resultId;
    private final int count;
    private String group;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private final List<NbtPredicateRecipe.RecipeSlot> ingredients = new ArrayList<>();

    public static NbtPredicateRecipeBuilder predicate(RecipeCategory category, Holder<Item> resultItem) {
        return new NbtPredicateRecipeBuilder(category, resultItem, 1);
    }

    public static NbtPredicateRecipeBuilder predicate(RecipeCategory category, Holder<Item> resultItem, int count) {
        return new NbtPredicateRecipeBuilder(category, resultItem, count);
    }

    public NbtPredicateRecipeBuilder(RecipeCategory category, Holder<Item> resultItem, int count) {
        this.category = category;
        this.resultItem = resultItem.value();
        this.resultId = resultItem.unwrapKey().orElseThrow().location();
        this.count = count;
        this.group = "";
    }

    public NbtPredicateRecipeBuilder add(int slot, Holder<Item> item, CompoundTag tag) {
        if (ingredients.stream().anyMatch(recipeSlot -> recipeSlot.slot() == slot))
            throw new IllegalArgumentException("Slot " + slot + " is already in this recipe");

        Optional<ResourceKey<Item>> optionalKey = item.unwrapKey();
        if (optionalKey.isEmpty())
            return this;
        NbtPredicateRecipe.RecipeSlot recipeSlot = new NbtPredicateRecipe.RecipeSlot(slot, optionalKey.get().location(), tag);
        this.ingredients.add(recipeSlot);
        return this;
    }

    public NbtPredicateRecipeBuilder add(int slot, Holder<Item> item) {
        return add(slot, item, new CompoundTag());
    }

    @Override
    public NbtPredicateRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public NbtPredicateRecipeBuilder group(@Nullable String groupName) {
        this.group = Objects.requireNonNullElse(groupName, "");
        return this;
    }

    @Override
    public Item getResult() {
        return resultItem;
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        finishedRecipeConsumer.accept(new Result(recipeId, resultId, count, determineBookCategory(category), group, advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/"), ingredients));
    }

    public static class Result extends CraftingResult {
        private final NbtPredicateRecipe recipe;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        protected Result(ResourceLocation id, ResourceLocation result, int count, CraftingBookCategory category, String group, Advancement.Builder advancement, ResourceLocation advancementId, List<NbtPredicateRecipe.RecipeSlot> ingredients) {
            super(category);
            this.recipe = new NbtPredicateRecipe(id, group, category, result, count, ingredients);
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            super.serializeRecipeData(json);
            DataResult<JsonElement> result = NbtPredicateRecipe.MAP_CODEC.encode(recipe, JsonOps.INSTANCE, JsonOps.INSTANCE.mapBuilder())
                    .build(json);
            JsonElement element = result.getOrThrow(false, NarakaMod.LOGGER::warn);
            for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                if (!json.has(entry.getKey()))
                    json.add(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public ResourceLocation getId() {
            return recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return NarakaRecipeSerializers.NBT_PREDICATE_RECIPE.value();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}

package com.yummy.naraka.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.crafting.SoulCraftingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NarakaRecipeProvider extends RecipeProvider {
    private static final List<ItemLike> NECTARIUM_SMELTABLES = List.of(NarakaBlocks.NECTARIUM_ORE, NarakaBlocks.DEEPSLATE_NECTARIUM_ORE);

    public NarakaRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, provider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.NECTARIUM, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.NECTARIUM_BLOCK);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, NarakaItems.SPEAR_ITEM)
                .define('/', Items.STICK)
                .define('=', NarakaItems.PURIFIED_SOUL_METAL)
                .pattern("  =")
                .pattern(" / ")
                .pattern("/  ")
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_METAL), has(NarakaItems.PURIFIED_SOUL_METAL))
                .save(recipeOutput);
        smithing(
                recipeOutput,
                NarakaItems.PURIFIED_GEMS_UPGRADE_SMITHING_TEMPLATE,
                NarakaItems.SPEAR_ITEM,
                NarakaItems.GOD_BLOOD,
                RecipeCategory.COMBAT,
                NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get()
        );
        oreSmelting(recipeOutput, NECTARIUM_SMELTABLES, RecipeCategory.MISC, NarakaItems.NECTARIUM, 0.7f, 200, "nectarium");
        oreBlasting(recipeOutput, NECTARIUM_SMELTABLES, RecipeCategory.MISC, NarakaItems.NECTARIUM, 0.7f, 100, "nectarium");

        soulCraftingRecipe(recipeOutput, Items.AMETHYST_SHARD, NarakaItems.SOUL_INFUSED_AMETHYST);
        soulCraftingRecipe(recipeOutput, Items.COPPER_INGOT, NarakaItems.SOUL_INFUSED_COPPER);
        soulCraftingRecipe(recipeOutput, Items.DIAMOND, NarakaItems.SOUL_INFUSED_DIAMOND);
        soulCraftingRecipe(recipeOutput, Items.EMERALD, NarakaItems.SOUL_INFUSED_EMERALD);
        soulCraftingRecipe(recipeOutput, Items.GOLD_INGOT, NarakaItems.SOUL_INFUSED_GOLD);
        soulCraftingRecipe(recipeOutput, Items.LAPIS_LAZULI, NarakaItems.SOUL_INFUSED_LAPIS);
        soulCraftingRecipe(recipeOutput, NarakaItems.NECTARIUM, NarakaItems.SOUL_INFUSED_NECTARIUM);
        soulCraftingRecipe(recipeOutput, Items.REDSTONE, NarakaItems.SOUL_INFUSED_REDSTONE);

        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_REDSTONE, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_COPPER, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_GOLD, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_EMERALD, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_DIAMOND, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_LAPIS, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_AMETHYST, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_NECTARIUM, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_METAL, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_SHARD, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.PURIFIED_SOUL_BLOCK);

        planksFromLog(recipeOutput, NarakaBlocks.EBONY_PLANKS, NarakaItemTags.EBONY_LOGS, 4);
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.EBONY_SLAB.get(), NarakaBlocks.EBONY_PLANKS);
        stairBuilder(NarakaBlocks.EBONY_STAIRS.get(), Ingredient.of(NarakaBlocks.EBONY_PLANKS.get()))
                .unlockedBy(getHasName(NarakaBlocks.EBONY_PLANKS), has(NarakaBlocks.EBONY_PLANKS))
                .save(recipeOutput);
    }

    protected static void smithing(RecipeOutput recipeOutput, ItemLike template, ItemLike base, ItemLike ingredient, RecipeCategory category, Item result) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(ingredient), category, result)
                .unlocks(getHasName(ingredient), has(ingredient))
                .save(recipeOutput, location(result, "_smithing"));
    }

    protected static ResourceLocation location(ItemLike item) {
        return NarakaMod.location(getItemName(item));
    }

    protected static ResourceLocation location(ItemLike item, String suffix) {
        return NarakaMod.location(getItemName(item) + suffix);
    }

    protected static void nineBlockStorageRecipes(
            RecipeOutput recipeOutput,
            RecipeCategory unpackedCategory,
            ItemLike unpacked,
            RecipeCategory packedCategory,
            ItemLike packed
    ) {
        ShapelessRecipeBuilder.shapeless(unpackedCategory, unpacked, 9)
                .requires(packed)
                .unlockedBy(getHasName(packed), has(packed))
                .save(recipeOutput, location(packed));
        ShapedRecipeBuilder.shaped(packedCategory, packed)
                .define('#', unpacked)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(unpacked), has(unpacked))
                .save(recipeOutput, location(unpacked, "_from_" + getItemName(packed)));
    }

        protected static void oreSmelting(
        RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup
    ) {
        NarakaRecipeProvider.oreCooking(
            pRecipeOutput,
            RecipeSerializer.SMELTING_RECIPE,
            SmeltingRecipe::new,
            pIngredients,
            pCategory,
            pResult,
            pExperience,
            pCookingTime,
            pGroup,
            "_from_smelting"
        );
    }

    protected static void oreBlasting(
        RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup
    ) {
        NarakaRecipeProvider.oreCooking(
            pRecipeOutput,
            RecipeSerializer.BLASTING_RECIPE,
            BlastingRecipe::new,
            pIngredients,
            pCategory,
            pResult,
            pExperience,
            pCookingTime,
            pGroup,
            "_from_blasting"
        );
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(
        RecipeOutput pRecipeOutput,
        RecipeSerializer<T> pSerializer,
        AbstractCookingRecipe.Factory<T> pRecipeFactory,
        List<ItemLike> pIngredients,
        RecipeCategory pCategory,
        ItemLike pResult,
        float pExperience,
        int pCookingTime,
        String pGroup,
        String pSuffix
    ) {
        for (ItemLike itemlike : pIngredients) {
            String name = getItemName(pResult) + pSuffix + "_" + getItemName(itemlike);
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pSerializer, pRecipeFactory)
                .group(pGroup)
                .unlockedBy(getHasName(itemlike), has(itemlike))
                .save(pRecipeOutput, NarakaMod.location(name));
        }
    }

    protected static void soulCraftingRecipe(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike result) {
        new SingleItemRecipeBuilder(RecipeCategory.MISC, SoulCraftingRecipe::new, Ingredient.of(ingredient), result, 1)
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_SHARD), has(NarakaItems.PURIFIED_SOUL_SHARD))
                .save(recipeOutput, location(result, "_soul_crafting"));
    }
}

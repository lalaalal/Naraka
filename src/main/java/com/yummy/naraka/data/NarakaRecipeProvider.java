package com.yummy.naraka.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.crafting.SoulCraftingRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NarakaRecipeProvider extends FabricRecipeProvider {
    private static final List<ItemLike> NECTARIUM_SMELTABLES = List.of(NarakaBlocks.NECTARIUM_ORE, NarakaBlocks.DEEPSLATE_NECTARIUM_ORE);

    public NarakaRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
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
                NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE,
                NarakaItems.SPEAR_ITEM,
                NarakaItems.GOD_BLOOD,
                RecipeCategory.COMBAT,
                NarakaItems.MIGHTY_HOLY_SPEAR_ITEM
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

        smithing(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, NarakaItems.PURIFIED_SOUL_SWORD, NarakaItems.SOUL_INFUSED_REDSTONE, RecipeCategory.COMBAT, NarakaItems.SOUL_INFUSED_REDSTONE_SWORD);
        smithing(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, NarakaItems.PURIFIED_SOUL_SWORD, NarakaItems.SOUL_INFUSED_COPPER, RecipeCategory.COMBAT, NarakaItems.SOUL_INFUSED_COPPER_SWORD);
        smithing(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, NarakaItems.PURIFIED_SOUL_SWORD, NarakaItems.SOUL_INFUSED_GOLD, RecipeCategory.COMBAT, NarakaItems.SOUL_INFUSED_GOLD_SWORD);
        smithing(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, NarakaItems.PURIFIED_SOUL_SWORD, NarakaItems.SOUL_INFUSED_EMERALD, RecipeCategory.COMBAT, NarakaItems.SOUL_INFUSED_EMERALD_SWORD);
        smithing(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, NarakaItems.PURIFIED_SOUL_SWORD, NarakaItems.SOUL_INFUSED_DIAMOND, RecipeCategory.COMBAT, NarakaItems.SOUL_INFUSED_DIAMOND_SWORD);
        smithing(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, NarakaItems.PURIFIED_SOUL_SWORD, NarakaItems.SOUL_INFUSED_LAPIS, RecipeCategory.COMBAT, NarakaItems.SOUL_INFUSED_LAPIS_SWORD);
        smithing(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, NarakaItems.PURIFIED_SOUL_SWORD, NarakaItems.SOUL_INFUSED_AMETHYST, RecipeCategory.COMBAT, NarakaItems.SOUL_INFUSED_AMETHYST_SWORD);
        smithing(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, NarakaItems.PURIFIED_SOUL_SWORD, NarakaItems.SOUL_INFUSED_NECTARIUM, RecipeCategory.COMBAT, NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD);
        smithing(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, NarakaItems.EBONY_SWORD, NarakaItems.PURIFIED_SOUL_METAL, RecipeCategory.COMBAT, NarakaItems.PURIFIED_SOUL_SWORD);

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

        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.HARD_EBONY_PLANKS, NarakaBlocks.EBONY_LOG, 1);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.HARD_EBONY_PLANKS, NarakaBlocks.EBONY_WOOD, 1);
        sword(recipeOutput, NarakaBlocks.HARD_EBONY_PLANKS, NarakaItems.EBONY_SWORD);

        nineBlockStorageRecipes(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.IRON_BLOCK.asItem(), RecipeCategory.MISC, NarakaItems.COMPRESSED_IRON_INGOT);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.COMPRESSED_IRON_INGOT, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.COMPRESSED_IRON_BLOCK);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, Items.AMETHYST_SHARD, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.AMETHYST_SHARD_BLOCK);
        smeltingResultFromBase(recipeOutput, NarakaItems.EBONY_ROOTS_SCRAP, NarakaBlocks.EBONY_ROOTS);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NarakaItems.EBONY_METAL_INGOT)
                .requires(NarakaItems.EBONY_ROOTS_SCRAP, 4)
                .requires(Items.NETHERITE_SCRAP, 4)
                .unlockedBy(getHasName(NarakaItems.EBONY_ROOTS_SCRAP), has(NarakaItems.EBONY_ROOTS_SCRAP))
                .save(recipeOutput);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.EBONY_METAL_INGOT, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.EBONY_METAL_BLOCK);
        copySmithingTemplate(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, NarakaItems.EBONY_METAL_INGOT, Blocks.IRON_BLOCK);
        trimSmithing(recipeOutput, NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE);
        helmet(recipeOutput, NarakaItems.PURIFIED_SOUL_METAL, NarakaItems.PURIFIED_SOUL_HELMET);
        chestplate(recipeOutput, NarakaItems.PURIFIED_SOUL_METAL, NarakaItems.PURIFIED_SOUL_CHESTPLATE);
        legging(recipeOutput, NarakaItems.PURIFIED_SOUL_METAL, NarakaItems.PURIFIED_SOUL_LEGGINGS);
        boots(recipeOutput, NarakaItems.PURIFIED_SOUL_METAL, NarakaItems.PURIFIED_SOUL_BOOTS);
        helmet(recipeOutput, NarakaItems.EBONY_METAL_INGOT, NarakaItems.EBONY_METAL_HELMET);
        chestplate(recipeOutput, NarakaItems.EBONY_METAL_INGOT, NarakaItems.EBONY_METAL_CHESTPLATE);
        legging(recipeOutput, NarakaItems.EBONY_METAL_INGOT, NarakaItems.EBONY_METAL_LEGGINGS);
        boots(recipeOutput, NarakaItems.EBONY_METAL_INGOT, NarakaItems.EBONY_METAL_BOOTS);
    }

    protected static void helmet(RecipeOutput recipeOutput, ItemLike material, ArmorItem helmet) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet)
                .define('X', material)
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy("has_diamond", has(material))
                .save(recipeOutput);
    }

    protected static void chestplate(RecipeOutput recipeOutput, ItemLike material, ArmorItem chestplate) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate)
                .define('X', material)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_diamond", has(material))
                .save(recipeOutput);
    }

    protected static void legging(RecipeOutput recipeOutput, ItemLike material, ArmorItem legging) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, legging)
                .define('X', material)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_diamond", has(material))
                .save(recipeOutput);
    }

    protected static void boots(RecipeOutput recipeOutput, ItemLike material, ArmorItem boots) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots)
                .define('X', material)
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_diamond", has(material))
                .save(recipeOutput);
    }

    protected static void trimSmithing(RecipeOutput recipeOutput, Item item) {
        trimSmithing(recipeOutput, item, location(item, "_smithing_trim"));
    }

    protected static ResourceLocation location(ItemLike item) {
        return NarakaMod.location(getItemName(item));
    }

    protected static ResourceLocation location(ItemLike item, String suffix) {
        return NarakaMod.location(getItemName(item) + suffix);
    }

    public static void copySmithingTemplate(RecipeOutput recipeOutput, ItemLike templateItem, ItemLike ingredient, ItemLike core) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, templateItem, 2)
                .define('#', ingredient)
                .define('C', core)
                .define('S', templateItem)
                .pattern("#S#")
                .pattern("#C#")
                .pattern("###")
                .unlockedBy(getHasName(templateItem), has(templateItem))
                .save(recipeOutput);
    }

    public static void sword(RecipeOutput recipeOutput, ItemLike material, ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('/', Items.STICK)
                .define('M', material)
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" / ")
                .unlockedBy(getHasName(material), has(material))
                .save(recipeOutput);
    }

    public static void smithing(RecipeOutput recipeOutput, ItemLike template, ItemLike base, ItemLike ingredient, RecipeCategory category, Item result) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(ingredient), category, result)
                .unlocks(getHasName(ingredient), has(ingredient))
                .save(recipeOutput, location(result, "_smithing"));
    }

    public static void stonecutterResultFromBase(RecipeOutput pRecipeOutput, RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial, int pResultCount) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(pMaterial), pCategory, pResult, pResultCount)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pRecipeOutput, NarakaMod.location(getConversionRecipeName(pResult, pMaterial) + "_stonecutting"));
    }

    public static void nineBlockStorageRecipes(
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

    public static void oreSmelting(
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

    public static void oreBlasting(
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

    public static <T extends AbstractCookingRecipe> void oreCooking(
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

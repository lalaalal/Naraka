package com.yummy.naraka.fabric.data;

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
    private static final List<ItemLike> NECTARIUM_SMELTABLES = List.of(NarakaBlocks.NECTARIUM_ORE.get(), NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get());

    public NarakaRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.NECTARIUM.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.NECTARIUM_BLOCK.get());
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, NarakaItems.SPEAR_ITEM.get())
                .define('/', Items.STICK)
                .define('=', NarakaItems.PURIFIED_SOUL_METAL.get())
                .pattern("  =")
                .pattern(" / ")
                .pattern("/  ")
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_METAL.get()), has(NarakaItems.PURIFIED_SOUL_METAL.get()))
                .save(recipeOutput);
        smithing(
                recipeOutput,
                NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get(),
                NarakaItems.SPEAR_ITEM.get(),
                NarakaItems.GOD_BLOOD.get(),
                RecipeCategory.COMBAT,
                NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get()
        );
        oreSmelting(recipeOutput, NECTARIUM_SMELTABLES, RecipeCategory.MISC, NarakaItems.NECTARIUM.get(), 0.7f, 200, "nectarium");
        oreBlasting(recipeOutput, NECTARIUM_SMELTABLES, RecipeCategory.MISC, NarakaItems.NECTARIUM.get(), 0.7f, 100, "nectarium");

        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_SHARD.get(), RecipeCategory.BUILDING_BLOCKS, NarakaItems.PURIFIED_SOUL_METAL.get());
        soulCraftingRecipe(recipeOutput, Items.AMETHYST_SHARD, NarakaItems.SOUL_INFUSED_AMETHYST.get());
        soulCraftingRecipe(recipeOutput, Items.COPPER_INGOT, NarakaItems.SOUL_INFUSED_COPPER.get());
        soulCraftingRecipe(recipeOutput, Items.DIAMOND, NarakaItems.SOUL_INFUSED_DIAMOND.get());
        soulCraftingRecipe(recipeOutput, Items.EMERALD, NarakaItems.SOUL_INFUSED_EMERALD.get());
        soulCraftingRecipe(recipeOutput, Items.GOLD_INGOT, NarakaItems.SOUL_INFUSED_GOLD.get());
        soulCraftingRecipe(recipeOutput, Items.LAPIS_LAZULI, NarakaItems.SOUL_INFUSED_LAPIS.get());
        soulCraftingRecipe(recipeOutput, NarakaItems.NECTARIUM.get(), NarakaItems.SOUL_INFUSED_NECTARIUM.get());
        soulCraftingRecipe(recipeOutput, Items.REDSTONE, NarakaItems.SOUL_INFUSED_REDSTONE.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, NarakaItems.PURIFIED_SOUL_SWORD.get())
                .define('M', NarakaItems.PURIFIED_SOUL_METAL.get())
                .define('/', Items.STICK)
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" / ")
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_METAL.get()), has(NarakaItems.PURIFIED_SOUL_METAL.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NarakaBlocks.HEROBRINE_TOTEM.get(), 2)
                .define('G', NarakaBlocks.IMITATION_GOLD_BLOCK.get())
                .define('T', NarakaBlocks.HEROBRINE_TOTEM.get())
                .pattern("GGG")
                .pattern("GTG")
                .pattern("GGG")
                .unlockedBy(getHasName(NarakaBlocks.HEROBRINE_TOTEM.get()), has(NarakaBlocks.HEROBRINE_TOTEM.get()))
                .save(recipeOutput);

        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_REDSTONE.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_COPPER.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_GOLD.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_EMERALD.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_DIAMOND.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_LAPIS.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_AMETHYST.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_NECTARIUM.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_METAL.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_SHARD.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.PURIFIED_SOUL_BLOCK.get());

        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.HARD_EBONY_PLANKS.get(), NarakaBlocks.EBONY_LOG.get(), 1);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.HARD_EBONY_PLANKS.get(), NarakaBlocks.EBONY_WOOD.get(), 1);
        sword(recipeOutput, NarakaBlocks.HARD_EBONY_PLANKS.get(), NarakaItems.EBONY_SWORD.get());

        nineBlockStorageRecipes(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.IRON_BLOCK.asItem(), RecipeCategory.MISC, NarakaItems.COMPRESSED_IRON_INGOT.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.COMPRESSED_IRON_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.COMPRESSED_IRON_BLOCK.get());
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, Items.AMETHYST_SHARD, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.AMETHYST_SHARD_BLOCK.get());
        smeltingResultFromBase(recipeOutput, NarakaItems.EBONY_ROOTS_SCRAP.get(), NarakaBlocks.EBONY_ROOTS.get());
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NarakaItems.EBONY_METAL_INGOT.get())
                .requires(NarakaItems.EBONY_ROOTS_SCRAP.get(), 4)
                .requires(Items.NETHERITE_SCRAP, 4)
                .unlockedBy(getHasName(NarakaItems.EBONY_ROOTS_SCRAP.get()), has(NarakaItems.EBONY_ROOTS_SCRAP.get()))
                .save(recipeOutput);
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.EBONY_METAL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.EBONY_METAL_BLOCK.get());
        copySmithingTemplate(recipeOutput, NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get(), NarakaItems.EBONY_METAL_INGOT.get(), NarakaBlocks.COMPRESSED_IRON_BLOCK.get());

        helmet(recipeOutput, NarakaItems.PURIFIED_SOUL_METAL.get(), NarakaItems.PURIFIED_SOUL_HELMET.get());
        chestplate(recipeOutput, NarakaItems.PURIFIED_SOUL_METAL.get(), NarakaItems.PURIFIED_SOUL_CHESTPLATE.get());
        legging(recipeOutput, NarakaItems.PURIFIED_SOUL_METAL.get(), NarakaItems.PURIFIED_SOUL_LEGGINGS.get());
        boots(recipeOutput, NarakaItems.PURIFIED_SOUL_METAL.get(), NarakaItems.PURIFIED_SOUL_BOOTS.get());
        helmet(recipeOutput, NarakaItems.EBONY_METAL_INGOT.get(), NarakaItems.EBONY_METAL_HELMET.get());
        chestplate(recipeOutput, NarakaItems.EBONY_METAL_INGOT.get(), NarakaItems.EBONY_METAL_CHESTPLATE.get());
        legging(recipeOutput, NarakaItems.EBONY_METAL_INGOT.get(), NarakaItems.EBONY_METAL_LEGGINGS.get());
        boots(recipeOutput, NarakaItems.EBONY_METAL_INGOT.get(), NarakaItems.EBONY_METAL_BOOTS.get());
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NarakaBlocks.SOUL_CRAFTING_BLOCK.get())
                .define('m', NarakaItems.EBONY_METAL_INGOT.get())
                .define('#', Items.BLAST_FURNACE)
                .define('B', Items.DEEPSLATE_TILES)
                .pattern("mmm")
                .pattern("m#m")
                .pattern("BBB")
                .unlockedBy(getHasName(NarakaItems.EBONY_METAL_INGOT.get()), has(NarakaItems.EBONY_METAL_INGOT.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NarakaBlocks.SOUL_STABILIZER.get())
                .define('#', Blocks.GLASS_PANE)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(Blocks.GLASS_PANE), has(Blocks.GLASS_PANE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NarakaBlocks.SOUL_SMITHING_BLOCK.get())
                .define('#', Items.HEAVY_CORE)
                .define('B', Blocks.SMITHING_TABLE)
                .pattern("##")
                .pattern("BB")
                .pattern("BB")
                .unlockedBy(getHasName(Items.HEAVY_CORE), has(Items.HEAVY_CORE))
                .save(recipeOutput);
    }

    protected static void helmet(RecipeOutput recipeOutput, ItemLike material, ArmorItem helmet) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet)
                .define('X', material)
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy(getHasName(material), has(material))
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

    protected static void trimSmithing(RecipeOutput recipeOutput, ItemLike item, Ingredient base, Ingredient material) {
        SmithingTrimRecipeBuilder.smithingTrim(Ingredient.of(item), base, material, RecipeCategory.MISC)
                .unlocks("has_smithing_trim_template", has(item))
                .save(recipeOutput, location(item, "_smithing_trim"));
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
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_SHARD.get()), has(NarakaItems.PURIFIED_SOUL_SHARD.get()))
                .save(recipeOutput, location(result, "_soul_crafting"));
    }
}

package com.yummy.naraka.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.block.NarakaBlocks;
import com.yummy.naraka.item.NarakaItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_SHARD, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.PURIFIED_SOUL_BLOCK);
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
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(packedCategory, packed)
                .define('#', unpacked)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(unpacked), has(unpacked))
                .save(recipeOutput);
    }

}

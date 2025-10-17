package com.yummy.naraka.fabric.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.component.AnyPredicate;
import com.yummy.naraka.core.component.BlessedPredicate;
import com.yummy.naraka.core.component.NarakaDataComponentPredicates;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class NarakaRecipeProvider extends RecipeProvider {
    private static final List<ItemLike> NECTARIUM_SMELTABLES = List.of(NarakaBlocks.NECTARIUM_ORE.get(), NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get());
    private static final List<ItemLike> AMETHYST_SMELTABLES = List.of(NarakaBlocks.AMETHYST_ORE.get(), NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get());

    private final HolderGetter<Item> items;

    protected NarakaRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
        items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void buildRecipes() {
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.NECTARIUM.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.NECTARIUM_BLOCK.get());
        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, NarakaItems.SPEAR_ITEM.get())
                .define('/', Items.STICK)
                .define('=', NarakaItems.PURIFIED_SOUL_METAL.get())
                .pattern("  =")
                .pattern(" / ")
                .pattern("/  ")
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_METAL.get()), has(NarakaItems.PURIFIED_SOUL_METAL.get()))
                .save(output);
        smithing(
                NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get(),
                NarakaItems.SPEAR_ITEM.get(),
                NarakaItems.GOD_BLOOD.get(),
                RecipeCategory.COMBAT,
                NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get()
        );
        purifiedSoulArmor(Items.CHAINMAIL_HELMET, NarakaItems.PURIFIED_SOUL_HELMET.get());
        purifiedSoulArmor(Items.CHAINMAIL_CHESTPLATE, NarakaItems.PURIFIED_SOUL_CHESTPLATE.get());
        purifiedSoulArmor(Items.CHAINMAIL_LEGGINGS, NarakaItems.PURIFIED_SOUL_LEGGINGS.get());
        purifiedSoulArmor(Items.CHAINMAIL_BOOTS, NarakaItems.PURIFIED_SOUL_BOOTS.get());

        oreSmelting(NECTARIUM_SMELTABLES, RecipeCategory.MISC, NarakaItems.NECTARIUM.get(), 0.7f, 200, "nectarium");
        oreBlasting(NECTARIUM_SMELTABLES, RecipeCategory.MISC, NarakaItems.NECTARIUM.get(), 0.7f, 100, "nectarium");
        oreSmelting(AMETHYST_SMELTABLES, RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.7f, 200, "amethyst");
        oreBlasting(AMETHYST_SMELTABLES, RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.7f, 100, "amethyst");

        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_SHARD.get(), RecipeCategory.BUILDING_BLOCKS, NarakaItems.PURIFIED_SOUL_METAL.get());

        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, NarakaItems.PURIFIED_SOUL_SWORD.get())
                .define('M', NarakaItems.PURIFIED_SOUL_METAL.get())
                .define('/', Items.STICK)
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" / ")
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_METAL.get()), has(NarakaItems.PURIFIED_SOUL_METAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, NarakaBlocks.HEROBRINE_TOTEM.get(), 2)
                .define('G', NarakaBlocks.IMITATION_GOLD_BLOCK.get())
                .define('T', NarakaBlocks.HEROBRINE_TOTEM.get())
                .pattern("GGG")
                .pattern("GTG")
                .pattern("GGG")
                .unlockedBy(getHasName(NarakaBlocks.HEROBRINE_TOTEM.get()), has(NarakaBlocks.HEROBRINE_TOTEM.get()))
                .save(output);

        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_REDSTONE.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK.get());
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_COPPER.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK.get());
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_GOLD.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK.get());
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_EMERALD.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK.get());
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_DIAMOND.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK.get());
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_LAPIS.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK.get());
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_AMETHYST.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK.get());
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_NECTARIUM.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK.get());
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_METAL.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get());

        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.HARD_EBONY_PLANKS.get(), NarakaBlocks.EBONY_LOG.get(), 1);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.HARD_EBONY_PLANKS.get(), NarakaBlocks.EBONY_WOOD.get(), 1);
        sword(NarakaBlocks.HARD_EBONY_PLANKS.get(), NarakaItems.ULTIMATE_SWORD.get());

        nineBlockStorageRecipes(RecipeCategory.BUILDING_BLOCKS, Blocks.IRON_BLOCK.asItem(), RecipeCategory.MISC, NarakaItems.COMPRESSED_IRON_INGOT.get());
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.COMPRESSED_IRON_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.COMPRESSED_IRON_BLOCK.get());
        nineBlockStorageRecipes(RecipeCategory.MISC, Items.AMETHYST_SHARD, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.AMETHYST_SHARD_BLOCK.get());
        smeltingResultFromBase(NarakaItems.EBONY_ROOTS_SCRAP.get(), NarakaBlocks.EBONY_ROOTS.get());
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, NarakaItems.EBONY_METAL_INGOT.get())
                .requires(NarakaItems.EBONY_ROOTS_SCRAP.get(), 4)
                .requires(Items.NETHERITE_SCRAP, 4)
                .unlockedBy(getHasName(NarakaItems.EBONY_ROOTS_SCRAP.get()), has(NarakaItems.EBONY_ROOTS_SCRAP.get()))
                .save(output);
        nineBlockStorageRecipes(RecipeCategory.MISC, NarakaItems.EBONY_METAL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.EBONY_METAL_BLOCK.get());
        copySmithingTemplate(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get(), NarakaItems.EBONY_METAL_INGOT.get(), NarakaBlocks.COMPRESSED_IRON_BLOCK.get());

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, NarakaBlocks.SOUL_STABILIZER.get())
                .define('#', Blocks.GLASS_PANE)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(Blocks.GLASS_PANE), has(Blocks.GLASS_PANE))
                .save(output);
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, NarakaBlocks.SOUL_SMITHING_BLOCK.get())
                .define('#', Items.HEAVY_CORE)
                .define('B', Blocks.SMITHING_TABLE)
                .pattern("##")
                .pattern("BB")
                .pattern("BB")
                .unlockedBy(getHasName(Items.HEAVY_CORE), has(Items.HEAVY_CORE))
                .save(output);

        soulInfusedMaterial(Items.REDSTONE, NarakaItems.SOUL_INFUSED_REDSTONE.get());
        soulInfusedMaterial(Items.COPPER_INGOT, NarakaItems.SOUL_INFUSED_COPPER.get());
        soulInfusedMaterial(Items.GOLD_INGOT, NarakaItems.SOUL_INFUSED_GOLD.get());
        soulInfusedMaterial(Items.EMERALD, NarakaItems.SOUL_INFUSED_EMERALD.get());
        soulInfusedMaterial(Items.DIAMOND, NarakaItems.SOUL_INFUSED_DIAMOND.get());
        soulInfusedMaterial(Items.LAPIS_LAZULI, NarakaItems.SOUL_INFUSED_LAPIS.get());
        soulInfusedMaterial(Items.AMETHYST_SHARD, NarakaItems.SOUL_INFUSED_AMETHYST.get());
        soulInfusedMaterial(NarakaItems.NECTARIUM.get(), NarakaItems.SOUL_INFUSED_NECTARIUM.get());

        ComponentPredicateRecipeBuilder.predicate(items, RecipeCategory.COMBAT, NarakaItems.SPEAR_OF_LONGINUS_ITEM)
                .requires(0, 0, NarakaItems.SOUL_INFUSED_REDSTONE_SWORD.get(), NarakaDataComponentPredicates.BLESSED.get(), BlessedPredicate.BLESSED)
                .requires(0, 1, NarakaItems.SOUL_INFUSED_COPPER_SWORD.get(), NarakaDataComponentPredicates.BLESSED.get(), BlessedPredicate.BLESSED)
                .requires(0, 2, NarakaItems.SOUL_INFUSED_GOLD_SWORD.get(), NarakaDataComponentPredicates.BLESSED.get(), BlessedPredicate.BLESSED)
                .requires(1, 0, NarakaItems.SOUL_INFUSED_EMERALD_SWORD.get(), NarakaDataComponentPredicates.BLESSED.get(), BlessedPredicate.BLESSED)
                .requires(1, 1, NarakaItems.GOD_BLOOD.get(), NarakaDataComponentPredicates.ANY.get(), AnyPredicate.INSTANCE)
                .requires(1, 2, NarakaItems.SOUL_INFUSED_DIAMOND_SWORD.get(), NarakaDataComponentPredicates.BLESSED.get(), BlessedPredicate.BLESSED)
                .requires(2, 0, NarakaItems.SOUL_INFUSED_LAPIS_SWORD.get(), NarakaDataComponentPredicates.BLESSED.get(), BlessedPredicate.BLESSED)
                .requires(2, 1, NarakaItems.SOUL_INFUSED_AMETHYST_SWORD.get(), NarakaDataComponentPredicates.BLESSED.get(), BlessedPredicate.BLESSED)
                .requires(2, 2, NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD.get(), NarakaDataComponentPredicates.BLESSED.get(), BlessedPredicate.BLESSED)
                .showNotification()
                .unlockedBy(getHasName(NarakaItems.GOD_BLOOD.get()), has(NarakaItems.GOD_BLOOD.get()))
                .save(output);
    }

    protected void soulInfusedMaterial(ItemLike material, ItemLike result) {
        Item purifiedSoulShard = NarakaItems.PURIFIED_SOUL_SHARD.get();
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, result, 8)
                .define('P', purifiedSoulShard)
                .define('M', material)
                .pattern("MMM")
                .pattern("MPM")
                .pattern("MMM")
                .unlockedBy(getHasName(purifiedSoulShard), has(purifiedSoulShard))
                .save(output);
    }

    protected void purifiedSoulArmor(ItemLike base, Item result) {
        smithing(
                NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get(),
                base,
                NarakaItems.PURIFIED_SOUL_METAL.get(),
                RecipeCategory.COMBAT,
                result
        );
    }

    protected void helmet(ItemLike material, Item helmet) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, helmet)
                .define('X', material)
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    protected void chestplate(ItemLike material, Item chestplate) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, chestplate)
                .define('X', material)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_diamond", has(material))
                .save(output);
    }

    protected void legging(ItemLike material, Item legging) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, legging)
                .define('X', material)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_diamond", has(material))
                .save(output);
    }

    protected void boots(ItemLike material, Item boots) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, boots)
                .define('X', material)
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_diamond", has(material))
                .save(output);
    }

    protected static ResourceKey<Recipe<?>> key(String path) {
        return ResourceKey.create(Registries.RECIPE, NarakaMod.location(path));
    }

    protected static ResourceKey<Recipe<?>> key(ItemLike item) {
        return key(getItemName(item));
    }

    protected static ResourceKey<Recipe<?>> key(ItemLike item, String suffix) {
        return key(getItemName(item) + suffix);
    }

    public void copySmithingTemplate(ItemLike templateItem, ItemLike ingredient, ItemLike core) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, templateItem, 2)
                .define('#', ingredient)
                .define('C', core)
                .define('S', templateItem)
                .pattern("#S#")
                .pattern("#C#")
                .pattern("###")
                .unlockedBy(getHasName(templateItem), has(templateItem))
                .save(output);
    }

    public void sword(ItemLike material, ItemLike result) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, result)
                .define('/', Items.STICK)
                .define('M', material)
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" / ")
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    public void smithing(ItemLike template, ItemLike base, ItemLike ingredient, RecipeCategory category, Item result) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(ingredient), category, result)
                .unlocks(getHasName(ingredient), has(ingredient))
                .save(output, key(result, "_smithing"));
    }

    public void stonecutterResultFromBase(RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial, int pResultCount) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(pMaterial), pCategory, pResult, pResultCount)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(output, key(getConversionRecipeName(pResult, pMaterial) + "_stonecutting"));
    }

    public void nineBlockStorageRecipes(
            RecipeCategory unpackedCategory,
            ItemLike unpacked,
            RecipeCategory packedCategory,
            ItemLike packed
    ) {
        ShapelessRecipeBuilder.shapeless(items, unpackedCategory, unpacked, 9)
                .requires(packed)
                .unlockedBy(getHasName(packed), has(packed))
                .save(output, key(packed));
        ShapedRecipeBuilder.shaped(items, packedCategory, packed)
                .define('#', unpacked)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(unpacked), has(unpacked))
                .save(output, key(unpacked, "_from_" + getItemName(packed)));
    }
}

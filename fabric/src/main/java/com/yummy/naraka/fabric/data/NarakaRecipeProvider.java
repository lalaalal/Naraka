package com.yummy.naraka.fabric.data;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.crafting.NbtPredicateRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Consumer;

public class NarakaRecipeProvider extends FabricRecipeProvider {
    private static final List<ItemLike> NECTARIUM_SMELTABLES = List.of(NarakaBlocks.NECTARIUM_ORE.getConcreteValue(), NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.getConcreteValue());
    private static final List<ItemLike> AMETHYST_SMELTABLES = List.of(NarakaBlocks.AMETHYST_ORE.getConcreteValue(), NarakaBlocks.DEEPSLATE_AMETHYST_ORE.getConcreteValue());

    protected NarakaRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> output) {
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.NECTARIUM.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.NECTARIUM_BLOCK.getConcreteValue());
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, NarakaItems.SPEAR_ITEM.getConcreteValue())
                .define('/', Items.STICK)
                .define('=', NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue())
                .pattern("  =")
                .pattern(" / ")
                .pattern("/  ")
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue()), has(NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue()))
                .save(output);
        smithing(
                output,
                NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.getConcreteValue(),
                NarakaItems.SPEAR_ITEM.getConcreteValue(),
                NarakaItems.GOD_BLOOD.getConcreteValue(),
                RecipeCategory.COMBAT,
                NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.getConcreteValue()
        );
        purifiedSoulArmor(output, Items.CHAINMAIL_HELMET, NarakaItems.PURIFIED_SOUL_HELMET.getConcreteValue());
        purifiedSoulArmor(output, Items.CHAINMAIL_CHESTPLATE, NarakaItems.PURIFIED_SOUL_CHESTPLATE.getConcreteValue());
        purifiedSoulArmor(output, Items.CHAINMAIL_LEGGINGS, NarakaItems.PURIFIED_SOUL_LEGGINGS.getConcreteValue());
        purifiedSoulArmor(output, Items.CHAINMAIL_BOOTS, NarakaItems.PURIFIED_SOUL_BOOTS.getConcreteValue());

        oreSmelting(output, NECTARIUM_SMELTABLES, RecipeCategory.MISC, NarakaItems.NECTARIUM.getConcreteValue(), 0.7f, 200, "nectarium");
        oreBlasting(output, NECTARIUM_SMELTABLES, RecipeCategory.MISC, NarakaItems.NECTARIUM.getConcreteValue(), 0.7f, 100, "nectarium");
        oreSmelting(output, AMETHYST_SMELTABLES, RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.7f, 200, "amethyst");
        oreBlasting(output, AMETHYST_SMELTABLES, RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.7f, 100, "amethyst");

        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_SHARD.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue());

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, NarakaItems.PURIFIED_SOUL_SWORD.getConcreteValue())
                .define('M', NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue())
                .define('/', Items.STICK)
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" / ")
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue()), has(NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.HEROBRINE_TOTEM.getConcreteValue(), 2)
                .define('G', NarakaBlocks.IMITATION_GOLD_BLOCK.getConcreteValue())
                .define('T', NarakaBlocks.HEROBRINE_TOTEM.getConcreteValue())
                .pattern("GGG")
                .pattern("GTG")
                .pattern("GGG")
                .unlockedBy(getHasName(NarakaBlocks.HEROBRINE_TOTEM.getConcreteValue()), has(NarakaBlocks.HEROBRINE_TOTEM.getConcreteValue()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.HEROBRINE_TOTEM.getConcreteValue(), 1)
                .define('G', NarakaBlocks.IMITATION_GOLD_BLOCK.getConcreteValue())
                .define('T', Blocks.CHISELED_NETHER_BRICKS)
                .pattern("GGG")
                .pattern("GTG")
                .pattern("GGG")
                .unlockedBy(getHasName(NarakaBlocks.IMITATION_GOLD_BLOCK.getConcreteValue()), has(NarakaBlocks.IMITATION_GOLD_BLOCK.getConcreteValue()))
                .save(output, location(NarakaBlocks.HEROBRINE_TOTEM.getConcreteValue(), "_from_chiseled_nether_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.IMITATION_GOLD_BLOCK.getConcreteValue())
                .define('B', Items.BLAZE_ROD)
                .define('I', Blocks.IRON_BLOCK)
                .pattern(" B ")
                .pattern("BIB")
                .pattern(" B ")
                .unlockedBy(getHasName(Items.BLAZE_ROD), has(Items.BLAZE_ROD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, NarakaItems.NETHERITE_HAMMER.getConcreteValue())
                .define('B', Items.NETHERITE_BLOCK)
                .define('I', Items.NETHERITE_INGOT)
                .define('/', Items.STICK)
                .pattern("IBI")
                .pattern(" / ")
                .pattern(" / ")
                .unlockedBy(getHasName(Items.NETHERITE_BLOCK), has(Items.NETHERITE_BLOCK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NarakaBlocks.SOUL_SMITHING_BLOCK.getConcreteValue())
                .define('N', Items.NETHERITE_BLOCK)
                .define('S', Items.SMITHING_TABLE)
                .pattern("NN ")
                .pattern("SS ")
                .pattern("SS ")
                .unlockedBy(getHasName(Items.SMITHING_TABLE), has(Items.SMITHING_TABLE))
                .save(output);
        CompoundTag blessed = new CompoundTag();
        NarakaNbtUtils.store(blessed, NarakaItemUtils.TAG_BLESSED, Codec.BOOL, true);
        blessed.putBoolean(NarakaItemUtils.TAG_UNBREAKABLE, true);
        NbtPredicateRecipeBuilder.predicate(RecipeCategory.COMBAT, NarakaItems.SPEAR_OF_LONGINUS_ITEM)
                .add(0, NarakaItems.SOUL_INFUSED_REDSTONE_SWORD, blessed)
                .add(1, NarakaItems.SOUL_INFUSED_COPPER_SWORD, blessed)
                .add(2, NarakaItems.SOUL_INFUSED_GOLD_SWORD, blessed)
                .add(3, NarakaItems.SOUL_INFUSED_EMERALD_SWORD, blessed)
                .add(4, NarakaItems.MIGHTY_HOLY_SPEAR_ITEM)
                .add(5, NarakaItems.SOUL_INFUSED_DIAMOND_SWORD, blessed)
                .add(6, NarakaItems.SOUL_INFUSED_LAPIS_SWORD, blessed)
                .add(7, NarakaItems.SOUL_INFUSED_AMETHYST_SWORD, blessed)
                .add(8, NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD, blessed)
                .unlockedBy(getHasName(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.getConcreteValue()), has(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.getConcreteValue()))
                .save(output);

        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_REDSTONE.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK.getConcreteValue());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_COPPER.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK.getConcreteValue());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_GOLD.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK.getConcreteValue());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_EMERALD.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK.getConcreteValue());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_DIAMOND.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK.getConcreteValue());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_LAPIS.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK.getConcreteValue());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_AMETHYST.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK.getConcreteValue());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_NECTARIUM.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK.getConcreteValue());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.getConcreteValue());

        nineBlockStorageRecipes(output, RecipeCategory.MISC, Items.AMETHYST_SHARD, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.AMETHYST_SHARD_BLOCK.getConcreteValue());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NarakaBlocks.SOUL_STABILIZER.getConcreteValue())
                .define('#', Blocks.GLASS_PANE)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(Blocks.GLASS_PANE), has(Blocks.GLASS_PANE))
                .save(output);

        soulInfusedMaterial(output, Items.REDSTONE, NarakaItems.SOUL_INFUSED_REDSTONE.getConcreteValue());
        soulInfusedMaterial(output, Items.COPPER_INGOT, NarakaItems.SOUL_INFUSED_COPPER.getConcreteValue());
        soulInfusedMaterial(output, Items.GOLD_INGOT, NarakaItems.SOUL_INFUSED_GOLD.getConcreteValue());
        soulInfusedMaterial(output, Items.EMERALD, NarakaItems.SOUL_INFUSED_EMERALD.getConcreteValue());
        soulInfusedMaterial(output, Items.DIAMOND, NarakaItems.SOUL_INFUSED_DIAMOND.getConcreteValue());
        soulInfusedMaterial(output, Items.LAPIS_LAZULI, NarakaItems.SOUL_INFUSED_LAPIS.getConcreteValue());
        soulInfusedMaterial(output, Items.AMETHYST_SHARD, NarakaItems.SOUL_INFUSED_AMETHYST.getConcreteValue());
        soulInfusedMaterial(output, NarakaItems.NECTARIUM.getConcreteValue(), NarakaItems.SOUL_INFUSED_NECTARIUM.getConcreteValue());
    }

    protected void soulInfusedMaterial(Consumer<FinishedRecipe> output, ItemLike material, ItemLike result) {
        Item purifiedSoulShard = NarakaItems.PURIFIED_SOUL_SHARD.getConcreteValue();
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 8)
                .define('P', purifiedSoulShard)
                .define('M', material)
                .pattern("MMM")
                .pattern("MPM")
                .pattern("MMM")
                .unlockedBy(getHasName(purifiedSoulShard), has(purifiedSoulShard))
                .save(output);
    }

    protected void purifiedSoulArmor(Consumer<FinishedRecipe> output, ItemLike base, Item result) {
        smithing(
                output,
                NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.getConcreteValue(),
                base,
                NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue(),
                RecipeCategory.COMBAT,
                result
        );
    }

    protected void helmet(Consumer<FinishedRecipe> output, ItemLike material, Item helmet) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet)
                .define('X', material)
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    protected void chestplate(Consumer<FinishedRecipe> output, ItemLike material, Item chestplate) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate)
                .define('X', material)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_diamond", has(material))
                .save(output);
    }

    protected void legging(Consumer<FinishedRecipe> output, ItemLike material, Item legging) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, legging)
                .define('X', material)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_diamond", has(material))
                .save(output);
    }

    protected void boots(Consumer<FinishedRecipe> output, ItemLike material, Item boots) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots)
                .define('X', material)
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_diamond", has(material))
                .save(output);
    }

    protected static ResourceLocation location(String path) {
        return NarakaMod.location(path);
    }

    protected static ResourceLocation location(ItemLike item) {
        return location(getItemName(item));
    }

    protected static ResourceLocation location(ItemLike item, String suffix) {
        return location(getItemName(item) + suffix);
    }

    public void copySmithingTemplate(Consumer<FinishedRecipe> output, ItemLike templateItem, ItemLike ingredient, ItemLike core) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, templateItem, 2)
                .define('#', ingredient)
                .define('C', core)
                .define('S', templateItem)
                .pattern("#S#")
                .pattern("#C#")
                .pattern("###")
                .unlockedBy(getHasName(templateItem), has(templateItem))
                .save(output);
    }

    public void sword(Consumer<FinishedRecipe> output, ItemLike material, ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('/', Items.STICK)
                .define('M', material)
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" / ")
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    public void smithing(Consumer<FinishedRecipe> output, ItemLike template, ItemLike base, ItemLike ingredient, RecipeCategory category, Item result) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(ingredient), category, result)
                .unlocks(getHasName(ingredient), has(ingredient))
                .save(output, location(result, "_smithing"));
    }

    public static void nineBlockStorageRecipes(
            Consumer<FinishedRecipe> recipeOutput,
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
}

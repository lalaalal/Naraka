package com.yummy.naraka.fabric.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.component.BlessApplier;
import com.yummy.naraka.core.component.DataComponentApplier;
import com.yummy.naraka.core.component.NarakaDataComponentAppliers;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.crafting.ComponentPredicateRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NarakaRecipeProvider extends FabricRecipeProvider {
    private static final List<ItemLike> NECTARIUM_SMELTABLES = List.of(NarakaBlocks.NECTARIUM_ORE.get(), NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get());
    private static final List<ItemLike> AMETHYST_SMELTABLES = List.of(NarakaBlocks.AMETHYST_ORE.get(), NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get());

    protected NarakaRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.NECTARIUM.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.NECTARIUM_BLOCK.get());
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, NarakaItems.SPEAR_ITEM.get())
                .define('/', Items.STICK)
                .define('=', NarakaItems.PURIFIED_SOUL_METAL.get())
                .pattern("  =")
                .pattern(" / ")
                .pattern("/  ")
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_METAL.get()), has(NarakaItems.PURIFIED_SOUL_METAL.get()))
                .save(output);
        smithing(
                output,
                NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get(),
                NarakaItems.SPEAR_ITEM.get(),
                NarakaItems.GOD_BLOOD.get(),
                RecipeCategory.COMBAT,
                NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get()
        );
        purifiedSoulArmor(output, Items.CHAINMAIL_HELMET, NarakaItems.PURIFIED_SOUL_HELMET.get());
        purifiedSoulArmor(output, Items.CHAINMAIL_CHESTPLATE, NarakaItems.PURIFIED_SOUL_CHESTPLATE.get());
        purifiedSoulArmor(output, Items.CHAINMAIL_LEGGINGS, NarakaItems.PURIFIED_SOUL_LEGGINGS.get());
        purifiedSoulArmor(output, Items.CHAINMAIL_BOOTS, NarakaItems.PURIFIED_SOUL_BOOTS.get());

        oreSmelting(output, NECTARIUM_SMELTABLES, RecipeCategory.MISC, NarakaItems.NECTARIUM.get(), 0.7f, 200, "nectarium");
        oreBlasting(output, NECTARIUM_SMELTABLES, RecipeCategory.MISC, NarakaItems.NECTARIUM.get(), 0.7f, 100, "nectarium");
        oreSmelting(output, AMETHYST_SMELTABLES, RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.7f, 200, "amethyst");
        oreBlasting(output, AMETHYST_SMELTABLES, RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.7f, 100, "amethyst");

        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_SHARD.get(), RecipeCategory.BUILDING_BLOCKS, NarakaItems.PURIFIED_SOUL_METAL.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, NarakaItems.PURIFIED_SOUL_SWORD.get())
                .define('M', NarakaItems.PURIFIED_SOUL_METAL.get())
                .define('/', Items.STICK)
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" / ")
                .unlockedBy(getHasName(NarakaItems.PURIFIED_SOUL_METAL.get()), has(NarakaItems.PURIFIED_SOUL_METAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NarakaBlocks.HEROBRINE_TOTEM.get(), 2)
                .define('G', NarakaBlocks.IMITATION_GOLD_BLOCK.get())
                .define('T', NarakaBlocks.HEROBRINE_TOTEM.get())
                .pattern("GGG")
                .pattern("GTG")
                .pattern("GGG")
                .unlockedBy(getHasName(NarakaBlocks.HEROBRINE_TOTEM.get()), has(NarakaBlocks.HEROBRINE_TOTEM.get()))
                .save(output);

        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_REDSTONE.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_COPPER.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_GOLD.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_EMERALD.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_DIAMOND.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_LAPIS.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_AMETHYST.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.SOUL_INFUSED_NECTARIUM.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, NarakaItems.PURIFIED_SOUL_METAL.get(), RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get());

        nineBlockStorageRecipes(output, RecipeCategory.BUILDING_BLOCKS, Blocks.IRON_BLOCK.asItem(), RecipeCategory.MISC, NarakaItems.COMPRESSED_IRON_INGOT.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, Items.AMETHYST_SHARD, RecipeCategory.BUILDING_BLOCKS, NarakaBlocks.AMETHYST_SHARD_BLOCK.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NarakaBlocks.SOUL_STABILIZER.get())
                .define('#', Blocks.GLASS_PANE)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(Blocks.GLASS_PANE), has(Blocks.GLASS_PANE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NarakaBlocks.SOUL_SMITHING_BLOCK.get())
                .define('#', Items.HEAVY_CORE)
                .define('B', Blocks.SMITHING_TABLE)
                .pattern("##")
                .pattern("BB")
                .pattern("BB")
                .unlockedBy(getHasName(Items.HEAVY_CORE), has(Items.HEAVY_CORE))
                .save(output);

        soulInfusedMaterial(output, Items.REDSTONE, NarakaItems.SOUL_INFUSED_REDSTONE.get());
        soulInfusedMaterial(output, Items.COPPER_INGOT, NarakaItems.SOUL_INFUSED_COPPER.get());
        soulInfusedMaterial(output, Items.GOLD_INGOT, NarakaItems.SOUL_INFUSED_GOLD.get());
        soulInfusedMaterial(output, Items.EMERALD, NarakaItems.SOUL_INFUSED_EMERALD.get());
        soulInfusedMaterial(output, Items.DIAMOND, NarakaItems.SOUL_INFUSED_DIAMOND.get());
        soulInfusedMaterial(output, Items.LAPIS_LAZULI, NarakaItems.SOUL_INFUSED_LAPIS.get());
        soulInfusedMaterial(output, Items.AMETHYST_SHARD, NarakaItems.SOUL_INFUSED_AMETHYST.get());
        soulInfusedMaterial(output, NarakaItems.NECTARIUM.get(), NarakaItems.SOUL_INFUSED_NECTARIUM.get());

        DataComponentPredicate blessedPredicate = DataComponentPredicate.builder()
                .expect(NarakaDataComponentTypes.BLESSED.get(), true)
                .build();
        DataComponentApplier.Single<?> blessApplier = new DataComponentApplier.Single<>(NarakaDataComponentAppliers.BLESS.get(), BlessApplier.bless());
        ComponentPredicateRecipeBuilder.predicate(RecipeCategory.COMBAT, NarakaItems.SPEAR_OF_LONGINUS_ITEM)
                .requires(0, 0, NarakaItems.SOUL_INFUSED_REDSTONE_SWORD.get(), blessedPredicate, blessApplier)
                .requires(0, 1, NarakaItems.SOUL_INFUSED_COPPER_SWORD.get(), blessedPredicate, blessApplier)
                .requires(0, 2, NarakaItems.SOUL_INFUSED_GOLD_SWORD.get(), blessedPredicate, blessApplier)
                .requires(1, 0, NarakaItems.SOUL_INFUSED_EMERALD_SWORD.get(), blessedPredicate, blessApplier)
                .requires(1, 1, NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get(), DataComponentPredicate.EMPTY)
                .requires(1, 2, NarakaItems.SOUL_INFUSED_DIAMOND_SWORD.get(), blessedPredicate, blessApplier)
                .requires(2, 0, NarakaItems.SOUL_INFUSED_LAPIS_SWORD.get(), blessedPredicate, blessApplier)
                .requires(2, 1, NarakaItems.SOUL_INFUSED_AMETHYST_SWORD.get(), blessedPredicate, blessApplier)
                .requires(2, 2, NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD.get(), blessedPredicate, blessApplier)
                .showNotification()
                .unlockedBy(getHasName(NarakaItems.GOD_BLOOD.get()), has(NarakaItems.GOD_BLOOD.get()))
                .save(output);
    }

    protected void soulInfusedMaterial(RecipeOutput output, ItemLike material, ItemLike result) {
        Item purifiedSoulShard = NarakaItems.PURIFIED_SOUL_SHARD.get();
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 8)
                .define('P', purifiedSoulShard)
                .define('M', material)
                .pattern("MMM")
                .pattern("MPM")
                .pattern("MMM")
                .unlockedBy(getHasName(purifiedSoulShard), has(purifiedSoulShard))
                .save(output);
    }

    protected void purifiedSoulArmor(RecipeOutput output, ItemLike base, Item result) {
        smithing(
                output,
                NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get(),
                base,
                NarakaItems.PURIFIED_SOUL_METAL.get(),
                RecipeCategory.COMBAT,
                result
        );
    }

    protected void helmet(RecipeOutput output, ItemLike material, Item helmet) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet)
                .define('X', material)
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    protected void chestplate(RecipeOutput output, ItemLike material, Item chestplate) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate)
                .define('X', material)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_diamond", has(material))
                .save(output);
    }

    protected void legging(RecipeOutput output, ItemLike material, Item legging) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, legging)
                .define('X', material)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_diamond", has(material))
                .save(output);
    }

    protected void boots(RecipeOutput output, ItemLike material, Item boots) {
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

    public void copySmithingTemplate(RecipeOutput output, ItemLike templateItem, ItemLike ingredient, ItemLike core) {
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

    public void sword(RecipeOutput output, ItemLike material, ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .define('/', Items.STICK)
                .define('M', material)
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" / ")
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    public void smithing(RecipeOutput output, ItemLike template, ItemLike base, ItemLike ingredient, RecipeCategory category, Item result) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(ingredient), category, result)
                .unlocks(getHasName(ingredient), has(ingredient))
                .save(output, location(result, "_smithing"));
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
}

package com.yummy.naraka.fabric;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;

public class NarakaCreativeModTabs {
    public static final CreativeModeTab NARAKA_TAB = register("naraka", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.translatable("itemGroup.naraka"))
            .icon(NarakaItems.GOD_BLOOD.get()::getDefaultInstance)
            .displayItems(NarakaCreativeModTabs::createNarakaTab)
            .build()
    );
    public static final CreativeModeTab NARAKA_TEST_TAB = registerIfDev("naraka_test", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
            .title(Component.translatable("itemGroup.naraka.test"))
            .icon(NarakaItems.STIGMA_ROD.get()::getDefaultInstance)
            .displayItems(NarakaCreativeModTabs::createNarakaTestTab)
            .build()
    );

    private static CreativeModeTab register(String name, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, NarakaMod.location(name), tab);
    }

    private static CreativeModeTab registerIfDev(String name, CreativeModeTab tab) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment())
            return register(name, tab);
        return tab;
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register(NarakaCreativeModTabs::modifyBuildingBlocksTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
                .register(NarakaCreativeModTabs::modifyNaturalBlocksTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register(NarakaCreativeModTabs::modifyFoodAndDrinksTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
                .register(NarakaCreativeModTabs::modifyIngredientsTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS)
                .register(NarakaCreativeModTabs::modifySpawnEggsTab);
    }

    private static void createNarakaTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(NarakaItems.SOUL_INFUSED_REDSTONE_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_COPPER_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_GOLD_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_EMERALD_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_DIAMOND_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_LAPIS_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_AMETHYST_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD.get());
        output.accept(NarakaItems.PURIFIED_SOUL_SWORD.get());

        output.accept(NarakaItems.SOUL_INFUSED_REDSTONE.get());
        output.accept(NarakaItems.SOUL_INFUSED_COPPER.get());
        output.accept(NarakaItems.SOUL_INFUSED_GOLD.get());
        output.accept(NarakaItems.SOUL_INFUSED_EMERALD.get());
        output.accept(NarakaItems.SOUL_INFUSED_DIAMOND.get());
        output.accept(NarakaItems.SOUL_INFUSED_LAPIS.get());
        output.accept(NarakaItems.SOUL_INFUSED_AMETHYST.get());
        output.accept(NarakaItems.SOUL_INFUSED_NECTARIUM.get());
        output.accept(NarakaItems.PURIFIED_SOUL_METAL.get());

        output.accept(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK.get());
        output.accept(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get());

        output.accept(NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
        output.accept(NarakaItems.GOD_BLOOD.get());

        output.accept(NarakaItems.PURIFIED_SOUL_SHARD.get());
        output.accept(NarakaItems.PURIFIED_SOUL_HELMET.get());
        output.accept(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get());
        output.accept(NarakaItems.PURIFIED_SOUL_LEGGINGS.get());
        output.accept(NarakaItems.PURIFIED_SOUL_BOOTS.get());

        output.accept(NarakaItems.HEROBRINE_PHASE_1_DISC.get());
        output.accept(NarakaItems.HEROBRINE_PHASE_2_DISC.get());
        output.accept(NarakaItems.HEROBRINE_PHASE_3_DISC.get());
        output.accept(NarakaItems.HEROBRINE_PHASE_4_DISC.get());
        output.accept(NarakaItems.SANCTUARY_COMPASS.get());
        output.accept(NarakaBlocks.SOUL_CRAFTING_BLOCK.get());

        output.accept(NarakaBlocks.FORGING_BLOCK.get());
        output.accept(NarakaBlocks.NECTARIUM_CORE_BLOCK.get());
        output.accept(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get());
    }

    private static void createNarakaTestTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(NarakaItems.EBONY_SWORD.get());
        output.accept(NarakaItems.EBONY_METAL_HELMET.get());
        output.accept(NarakaItems.EBONY_METAL_CHESTPLATE.get());
        output.accept(NarakaItems.EBONY_METAL_LEGGINGS.get());
        output.accept(NarakaItems.EBONY_METAL_BOOTS.get());
        output.accept(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get());

        output.accept(NarakaItems.EBONY_METAL_INGOT.get());
        output.accept(NarakaItems.COMPRESSED_IRON_INGOT.get());

        output.accept(NarakaItems.SPEAR_ITEM.get());
        output.accept(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get());
        output.accept(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());

        output.accept(NarakaBlocks.EBONY_METAL_BLOCK.get());
        output.accept(NarakaBlocks.COMPRESSED_IRON_BLOCK.get());
        output.accept(NarakaBlocks.PURIFIED_SOUL_BLOCK.get());

        output.accept(NarakaBlocks.EBONY_LOG.get());
        output.accept(NarakaBlocks.EBONY_WOOD.get());
        output.accept(NarakaBlocks.STRIPPED_EBONY_LOG.get());
        output.accept(NarakaBlocks.STRIPPED_EBONY_WOOD.get());
        output.accept(NarakaBlocks.HARD_EBONY_PLANKS.get());
        output.accept(NarakaBlocks.EBONY_ROOTS.get());

        output.accept(NarakaItems.EBONY_ROOTS_SCRAP.get());
        output.accept(NarakaBlocks.EBONY_LEAVES.get());
        output.accept(NarakaBlocks.EBONY_SAPLING.get());

        output.accept(NarakaItems.STIGMA_ROD.get());

        output.accept(NarakaBlocks.NECTARIUM_ORE.get());
        output.accept(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get());
        output.accept(NarakaBlocks.NARAKA_FORGING_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_STABILIZER.get());
        output.accept(NarakaBlocks.SOUL_SMITHING_BLOCK.get());

        output.accept(blessed(NarakaItems.PURIFIED_SOUL_HELMET.get()));
        output.accept(blessed(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get()));
        output.accept(blessed(NarakaItems.PURIFIED_SOUL_LEGGINGS.get()));
        output.accept(blessed(NarakaItems.PURIFIED_SOUL_BOOTS.get()));
    }

    private static ItemStack blessed(Item item) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(NarakaDataComponentTypes.BLESSED.get(), true);
        return itemStack;
    }

    private static void modifyBuildingBlocksTab(FabricItemGroupEntries entries) {
        entries.addAfter(Blocks.AMETHYST_BLOCK,
                NarakaBlocks.AMETHYST_SHARD_BLOCK.get(),
                NarakaBlocks.NECTARIUM_BLOCK.get(),
                NarakaBlocks.IMITATION_GOLD_BLOCK.get()
        );
    }

    private static void modifyNaturalBlocksTab(FabricItemGroupEntries entries) {

    }

    private static void modifyFoodAndDrinksTab(FabricItemGroupEntries entries) {
        entries.addAfter(Items.ENCHANTED_GOLDEN_APPLE, NarakaItems.NECTARIUM.get());
    }

    private static void modifyIngredientsTab(FabricItemGroupEntries entries) {
        entries.addAfter(Items.DIAMOND, NarakaItems.NECTARIUM.get());
    }

    private static void modifySpawnEggsTab(FabricItemGroupEntries entries) {
        entries.addAfter(Blocks.TRIAL_SPAWNER, NarakaBlocks.HEROBRINE_TOTEM.get());
    }
}

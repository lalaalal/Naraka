package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class NarakaCreativeModTabs {
    public static final CreativeModeTab NARAKA_TAB = register("naraka", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.naraka"))
            .icon(NarakaItems.GOD_BLOOD::getDefaultInstance)
            .displayItems(NarakaCreativeModTabs::createNarakaTab)
            .build()
    );

    private static CreativeModeTab register(String name, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, NarakaMod.location(name), tab);
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register(NarakaCreativeModTabs::modifyBuildingBlocksTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
                .register(NarakaCreativeModTabs::modifyNaturalBlocksTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register(NarakaCreativeModTabs::modifyFoodAndDrinksTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS)
                .register(NarakaCreativeModTabs::modifySpawnEggsTab);
    }

    private static void createNarakaTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(NarakaItems.SOUL_INFUSED_REDSTONE_SWORD);
        output.accept(NarakaItems.SOUL_INFUSED_COPPER_SWORD);
        output.accept(NarakaItems.SOUL_INFUSED_GOLD_SWORD);
        output.accept(NarakaItems.SOUL_INFUSED_EMERALD_SWORD);
        output.accept(NarakaItems.SOUL_INFUSED_DIAMOND_SWORD);
        output.accept(NarakaItems.SOUL_INFUSED_LAPIS_SWORD);
        output.accept(NarakaItems.SOUL_INFUSED_AMETHYST_SWORD);
        output.accept(NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD);
        output.accept(NarakaItems.PURIFIED_SOUL_SWORD);

        output.accept(NarakaItems.SOUL_INFUSED_REDSTONE);
        output.accept(NarakaItems.SOUL_INFUSED_COPPER);
        output.accept(NarakaItems.SOUL_INFUSED_GOLD);
        output.accept(NarakaItems.SOUL_INFUSED_EMERALD);
        output.accept(NarakaItems.SOUL_INFUSED_DIAMOND);
        output.accept(NarakaItems.SOUL_INFUSED_LAPIS);
        output.accept(NarakaItems.SOUL_INFUSED_AMETHYST);
        output.accept(NarakaItems.SOUL_INFUSED_NECTARIUM);
        output.accept(NarakaItems.PURIFIED_SOUL_METAL);

        output.accept(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK);
        output.accept(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK);
        output.accept(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK);
        output.accept(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK);
        output.accept(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK);
        output.accept(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK);
        output.accept(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK);
        output.accept(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK);
        output.accept(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK);

        output.accept(NarakaItems.EBONY_SWORD);
        output.accept(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE);
        output.accept(NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE);
        output.accept(NarakaItems.EBONY_METAL_HELMET);
        output.accept(NarakaItems.EBONY_METAL_CHESTPLATE);
        output.accept(NarakaItems.EBONY_METAL_LEGGINGS);
        output.accept(NarakaItems.EBONY_METAL_BOOTS);
        output.accept(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM);
        output.accept(NarakaItems.GOD_BLOOD);

        output.accept(NarakaItems.EBONY_METAL_INGOT);
        output.accept(NarakaItems.COMPRESSED_IRON_INGOT);
        output.accept(NarakaItems.PURIFIED_SOUL_SHARD);
        output.accept(NarakaItems.PURIFIED_SOUL_HELMET);
        output.accept(NarakaItems.PURIFIED_SOUL_CHESTPLATE);
        output.accept(NarakaItems.PURIFIED_SOUL_LEGGINGS);
        output.accept(NarakaItems.PURIFIED_SOUL_BOOTS);
        output.accept(NarakaItems.SPEAR_ITEM);
        output.accept(NarakaItems.SPEAR_OF_LONGINUS_ITEM);

        output.accept(NarakaBlocks.EBONY_METAL_BLOCK);
        output.accept(NarakaBlocks.COMPRESSED_IRON_BLOCK);
        output.accept(NarakaBlocks.PURIFIED_SOUL_BLOCK);
        output.accept(NarakaBlocks.EBONY_LOG);
        output.accept(NarakaBlocks.EBONY_WOOD);
        output.accept(NarakaBlocks.STRIPPED_EBONY_LOG);
        output.accept(NarakaBlocks.STRIPPED_EBONY_WOOD);
        output.accept(NarakaBlocks.HARD_EBONY_PLANKS);
        output.accept(NarakaBlocks.EBONY_ROOTS);

        output.accept(NarakaItems.EBONY_ROOTS_SCRAP);
        output.accept(NarakaBlocks.EBONY_LEAVES);
        output.accept(NarakaBlocks.EBONY_SAPLING);
        output.accept(NarakaItems.HEROBRINE_PHASE_1_DISC);
        output.accept(NarakaItems.HEROBRINE_PHASE_2_DISC);
        output.accept(NarakaItems.HEROBRINE_PHASE_3_DISC);
        output.accept(NarakaItems.HEROBRINE_PHASE_4_DISC);
        output.accept(NarakaItems.SANCTUARY_COMPASS);
        output.accept(NarakaBlocks.SOUL_CRAFTING_BLOCK);
    }

    private static void modifyBuildingBlocksTab(FabricItemGroupEntries entries) {
        entries.addAfter(Blocks.AMETHYST_BLOCK,
                NarakaBlocks.AMETHYST_SHARD_BLOCK,
                NarakaBlocks.NECTARIUM_BLOCK,
                NarakaBlocks.FAKE_GOLD_BLOCK
        );
    }

    private static void modifyNaturalBlocksTab(FabricItemGroupEntries entries) {
        entries.addAfter(Blocks.DEEPSLATE_DIAMOND_ORE, NarakaBlocks.NECTARIUM_ORE, NarakaBlocks.DEEPSLATE_NECTARIUM_ORE);
    }

    private static void modifyFoodAndDrinksTab(FabricItemGroupEntries entries) {
        entries.addAfter(Items.ENCHANTED_GOLDEN_APPLE, NarakaItems.NECTARIUM);
    }

    private static void modifySpawnEggsTab(FabricItemGroupEntries entries) {
        entries.addAfter(Blocks.TRIAL_SPAWNER, NarakaBlocks.HEROBRINE_TOTEM);
    }
}

package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class NarakaCreativeModTabs {
    private static final CreativeModeTab NARAKA_TAB = register("example_tab", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.naraka"))
            .icon(() -> NarakaBlocks.HEROBRINE_TOTEM.asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(NarakaItems.SPEAR_ITEM);
                output.accept(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM);
                output.accept(NarakaItems.SPEAR_OF_LONGINUS_ITEM);
                output.accept(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE);
                output.accept(NarakaItems.PURIFIED_GEMS_UPGRADE_SMITHING_TEMPLATE);
                output.accept(NarakaBlocks.NECTARIUM_ORE);
                output.accept(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE);
                output.accept(NarakaItems.NECTARIUM);
                output.accept(NarakaItems.PURIFIED_SOUL_SHARD);

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

                output.accept(NarakaItems.GOD_BLOOD);
                output.accept(NarakaBlocks.AMETHYST_SHARD_BLOCK);
                output.accept(NarakaItems.COMPRESSED_IRON_INGOT);
                output.accept(NarakaBlocks.COMPRESSED_IRON_BLOCK);
                output.accept(NarakaItems.FAKE_GOLD_INGOT);
                output.accept(NarakaBlocks.FAKE_GOLD_BLOCK);
                output.accept(NarakaBlocks.HEROBRINE_TOTEM);
                output.accept(NarakaBlocks.PURIFIED_SOUL_BLOCK);
                output.accept(NarakaBlocks.NECTARIUM_BLOCK);
                output.accept(NarakaBlocks.EBONY_LOG);
                output.accept(NarakaBlocks.STRIPPED_EBONY_LOG);
                output.accept(NarakaBlocks.EBONY_WOOD);
                output.accept(NarakaBlocks.STRIPPED_EBONY_WOOD);
                output.accept(NarakaBlocks.EBONY_LEAVES);
                output.accept(NarakaBlocks.EBONY_SAPLING);
                output.accept(NarakaBlocks.EBONY_PLANKS);
                output.accept(NarakaBlocks.HARD_EBONY_PLANKS);
                output.accept(NarakaItems.EBONY_SWORD);
            }).build());

    private static CreativeModeTab register(String name, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, NarakaMod.location(name), tab);
    }

    public static void initialize() {
    }
}
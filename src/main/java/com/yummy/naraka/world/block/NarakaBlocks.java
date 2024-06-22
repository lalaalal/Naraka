package com.yummy.naraka.world.block;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Function;

public class NarakaBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NarakaMod.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NarakaMod.MOD_ID);

    public static final DeferredBlock<Block> TRANSPARENT_BLOCK = registerBlockWithItem(
            "transparent_block",
            properties -> new TransparentBlock(
                    properties
                            .noCollission()
                            .forceSolidOn()
                            .noLootTable()
            ),
            Blocks.BEDROCK
    );

    public static final DeferredBlock<Block> NECTARIUM_BLOCK = registerBlockWithItem(
            "nectarium_block", NectariumBlock::new, Blocks.IRON_BLOCK
    );

    public static final DeferredBlock<DropExperienceBlock> NECTARIUM_ORE = registerBlockWithItem(
            "nectarium_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            Blocks.IRON_ORE
    );

    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_NECTARIUM_ORE = registerBlockWithItem(
            "deepslate_nectarium_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            Blocks.DEEPSLATE_IRON_ORE
    );

    public static final DeferredBlock<Block> PURIFIED_SOUL_BLOCK = registerSimpleBlockWithItem(
            "purified_soul_block",
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK),
            new Item.Properties().fireResistant()
    );

    public static final DeferredBlock<BaseFireBlock> PURIFIED_SOUL_FIRE_BLOCK = registerBlockWithItem(
            "purified_soul_fire",
            properties -> new PurifiedSoulFireBlock(properties
                    .noLootTable()
                    .lightLevel(state -> 7)
                    .mapColor(MapColor.COLOR_BLACK)
            ),
            BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_FIRE),
            new Item.Properties()
    );

    public static final DeferredBlock<SoulCraftingBlock> SOUL_CRAFTING_BLOCK = registerBlockWithItem(
            "soul_crafting_block",
            SoulCraftingBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BLAST_FURNACE)
                    .lightLevel(SoulCraftingBlock::lightLevel)
    );

    public static final DeferredBlock<Block> SOUL_INFUSED_REDSTONE_BLOCK = registerSimpleBlockWithItem(
            "soul_infused_redstone_block",
            Blocks.REDSTONE_BLOCK
    );

    public static final DeferredBlock<Block> SOUL_INFUSED_COPPER_BLOCK = registerSimpleBlockWithItem(
            "soul_infused_copper_block",
            Blocks.COPPER_BLOCK
    );

    public static final DeferredBlock<Block> SOUL_INFUSED_GOLD_BLOCK = registerSimpleBlockWithItem(
            "soul_infused_gold_block",
            Blocks.GOLD_BLOCK
    );

    public static final DeferredBlock<Block> SOUL_INFUSED_EMERALD_BLOCK = registerSimpleBlockWithItem(
            "soul_infused_emerald_block",
            Blocks.EMERALD_BLOCK
    );

    public static final DeferredBlock<Block> SOUL_INFUSED_DIAMOND_BLOCK = registerSimpleBlockWithItem(
            "soul_infused_diamond_block",
            Blocks.DIAMOND_BLOCK
    );

    public static final DeferredBlock<Block> SOUL_INFUSED_LAPIS_BLOCK = registerSimpleBlockWithItem(
            "soul_infused_lapis_block",
            Blocks.LAPIS_BLOCK
    );

    public static final DeferredBlock<Block> SOUL_INFUSED_AMETHYST_BLOCK = registerSimpleBlockWithItem(
            "soul_infused_amethyst_block",
            Blocks.AMETHYST_BLOCK
    );

    public static final DeferredBlock<Block> SOUL_INFUSED_NECTARIUM_BLOCK = registerSimpleBlockWithItem(
            "soul_infused_nectarium_block",
            NECTARIUM_BLOCK.get()
    );

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        DeferredBlock<B> blockHolder = BLOCKS.registerBlock(name, function, blockProperties);
        ITEMS.registerSimpleBlockItem(blockHolder, itemProperties);
        return blockHolder;
    }

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties properties) {
        DeferredBlock<B> blockHolder = BLOCKS.registerBlock(name, function, properties);
        ITEMS.registerSimpleBlockItem(blockHolder);
        return blockHolder;
    }

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        return registerBlockWithItem(name, function, BlockBehaviour.Properties.ofFullCopy(propertyBase));
    }

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function) {
        return registerBlockWithItem(name, function, BlockBehaviour.Properties.of());
    }

    private static DeferredBlock<Block> registerSimpleBlockWithItem(String name, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        return registerBlockWithItem(name, Block::new, blockProperties, itemProperties);
    }

    private static DeferredBlock<Block> registerSimpleBlockWithItem(String name, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(name, Block::new, properties);
    }

    private static DeferredBlock<Block> registerSimpleBlockWithItem(String name, Block propertyBase) {
        return registerBlockWithItem(name, Block::new, BlockBehaviour.Properties.ofFullCopy(propertyBase));
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    public static List<Block> getKnownBlocks() {
        return BLOCKS.getEntries()
                .stream()
                .map(Holder::value)
                .toList();
    }
}

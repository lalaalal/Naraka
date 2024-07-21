package com.yummy.naraka.world.block;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class NarakaBlocks {
    public static final String SOUL_INFUSED_PREFIX = "soul_infused_";

    public static final TransparentBlock TRANSPARENT_BLOCK = registerBlockWithItem(
            "transparent_block",
            properties -> new TransparentBlock(
                    properties
                            .noCollission()
                            .forceSolidOn()
                            .noLootTable()
            ),
            Blocks.BEDROCK
    );

    public static final DropExperienceBlock NECTARIUM_ORE = registerBlockWithItem(
            "nectarium_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            Blocks.IRON_ORE
    );
    public static final DropExperienceBlock DEEPSLATE_NECTARIUM_ORE = registerBlockWithItem(
            "deepslate_nectarium_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            Blocks.DEEPSLATE_IRON_ORE
    );
    public static final Block NECTARIUM_BLOCK = registerBlockWithItem("nectarium_block", NectariumBlock::new, Blocks.IRON_BLOCK);
    public static final PurifiedSoulBlock PURIFIED_SOUL_BLOCK = registerBlockWithItem(
            "purified_soul_block",
            PurifiedSoulBlock::new,
            from(Blocks.SOUL_SAND)
                    .mapColor(DyeColor.WHITE)
                    .requiresCorrectToolForDrops(),
            item().fireResistant()
    );
    public static final Block PURIFIED_SOUL_METAL_BLOCK = registerSimpleBlockWithItem(
            "purified_soul_metal_block",
            from(Blocks.IRON_BLOCK)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .requiresCorrectToolForDrops(),
            item().fireResistant()
    );
    public static final BaseFireBlock PURIFIED_SOUL_FIRE_BLOCK = registerBlockWithItem(
            "purified_soul_fire",
            PurifiedSoulFireBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_FIRE)
                    .noLootTable()
                    .lightLevel(state -> 7)
                    .mapColor(MapColor.COLOR_BLACK),
            new Item.Properties()
    );
    public static final SoulCraftingBlock SOUL_CRAFTING_BLOCK = registerBlockWithItem(
            "soul_crafting_block",
            SoulCraftingBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BLAST_FURNACE)
                    .lightLevel(SoulCraftingBlock::lightLevel)
    );

    public static final Block COMPRESSED_IRON_BLOCK = registerSimpleBlockWithItem("compressed_iron_block", Blocks.IRON_BLOCK);
    public static final Block FAKE_GOLD_BLOCK = registerSimpleBlockWithItem("fake_gold_block", from(Blocks.GOLD_BLOCK).strength(5, 6));
    public static final Block AMETHYST_SHARD_BLOCK = registerSimpleBlockWithItem(
            "amethyst_shard_block",
            from(Blocks.AMETHYST_BLOCK).strength(0.5f)
    );

    public static final Block SOUL_INFUSED_REDSTONE_BLOCK = registerSoulInfusedBlock("redstone_block", Blocks.REDSTONE_BLOCK);
    public static final Block SOUL_INFUSED_COPPER_BLOCK = registerSoulInfusedBlock("copper_block", Blocks.COPPER_BLOCK);
    public static final Block SOUL_INFUSED_GOLD_BLOCK = registerSoulInfusedBlock("gold_block", Blocks.GOLD_BLOCK);
    public static final Block SOUL_INFUSED_EMERALD_BLOCK = registerSoulInfusedBlock("emerald_block", Blocks.EMERALD_BLOCK);
    public static final Block SOUL_INFUSED_DIAMOND_BLOCK = registerSoulInfusedBlock("diamond_block", Blocks.DIAMOND_BLOCK);
    public static final Block SOUL_INFUSED_LAPIS_BLOCK = registerSoulInfusedBlock("lapis_block", Blocks.LAPIS_BLOCK);
    public static final Block SOUL_INFUSED_AMETHYST_BLOCK = registerSoulInfusedBlock("amethyst_block", Blocks.AMETHYST_BLOCK);
    public static final Block SOUL_INFUSED_NECTARIUM_BLOCK = registerSoulInfusedBlock("nectarium_block", NECTARIUM_BLOCK);

    public static final RotatedPillarBlock EBONY_LOG = registerBlockWithItem("ebony_log", RotatedPillarBlock::new, Blocks.DARK_OAK_LOG);
    public static final RotatedPillarBlock STRIPPED_EBONY_LOG = registerBlockWithItem("stripped_ebony_log", RotatedPillarBlock::new, Blocks.STRIPPED_DARK_OAK_LOG);
    public static final RotatedPillarBlock EBONY_WOOD = registerBlockWithItem("ebony_wood", RotatedPillarBlock::new, Blocks.DARK_OAK_WOOD);
    public static final RotatedPillarBlock STRIPPED_EBONY_WOOD = registerBlockWithItem("stripped_ebony_wood", RotatedPillarBlock::new, Blocks.STRIPPED_DARK_OAK_WOOD);
    public static final LeavesBlock EBONY_LEAVES = registerBlockWithItem("ebony_leaves", LeavesBlock::new, Blocks.DARK_OAK_LEAVES);

    public static final Block EBONY_PLANKS = registerSimpleBlockWithItem("ebony_planks", Blocks.DARK_OAK_PLANKS);

    public static final EbonySaplingBlock EBONY_SAPLING = registerBlockWithItem("ebony_sapling", EbonySaplingBlock::new, Blocks.DARK_OAK_SAPLING);
    public static final Block HARD_EBONY_PLANKS = registerBlockWithItem(
            "hard_ebony_planks",
            properties -> new RotatedPillarBlock(properties.strength(40, 6)),
            Blocks.DARK_OAK_PLANKS
    );

    public static final HerobrineTotem HEROBRINE_TOTEM = registerBlockWithItem(
            "herobrine_totem",
            properties -> new HerobrineTotem(properties
                    .pushReaction(PushReaction.BLOCK)
                    .strength(50, 1200)
                    .noLootTable()
                    .lightLevel(HerobrineTotem::light)
            ),
            Blocks.NETHER_BRICKS
    );


    public static final List<Block> SOUL_INFUSED_BLOCKS = List.of(
            SOUL_INFUSED_REDSTONE_BLOCK,
            SOUL_INFUSED_COPPER_BLOCK,
            SOUL_INFUSED_GOLD_BLOCK,
            SOUL_INFUSED_EMERALD_BLOCK,
            SOUL_INFUSED_DIAMOND_BLOCK,
            SOUL_INFUSED_LAPIS_BLOCK,
            SOUL_INFUSED_AMETHYST_BLOCK,
            SOUL_INFUSED_NECTARIUM_BLOCK
    );

    public static void forEachSoulInfusedBlock(Consumer<Block> consumer) {
        for (Block soulInfusedBlock : SOUL_INFUSED_BLOCKS)
            consumer.accept(soulInfusedBlock);
    }


    public static BlockBehaviour.Properties from(Block block) {
        return BlockBehaviour.Properties.ofFullCopy(block);
    }

    public static Item.Properties item() {
        return new Item.Properties();
    }

    public static void setFlammableBlocks() {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(EBONY_LOG, 5, 5);
        fire.setFlammable(STRIPPED_EBONY_LOG, 5, 5);
        fire.setFlammable(EBONY_WOOD, 5, 5);
        fire.setFlammable(STRIPPED_EBONY_WOOD, 5, 5);
        fire.setFlammable(EBONY_LEAVES, 30, 60);
        fire.setFlammable(EBONY_PLANKS, 5, 20);
    }

    private static Block registerSoulInfusedBlock(String name, Block baseBlock) {
        return registerSimpleBlockWithItem(SOUL_INFUSED_PREFIX + name, baseBlock, item().fireResistant());
    }

    private static <B extends Block> B registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(propertyBase);
        return Registry.register(BuiltInRegistries.BLOCK, NarakaMod.location(name), function.apply(properties));
    }

    private static <B extends Block> B registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        B block = Registry.register(BuiltInRegistries.BLOCK, NarakaMod.location(name), function.apply(blockProperties));
        BlockItem blockItem = new BlockItem(block, itemProperties);
        Registry.register(BuiltInRegistries.ITEM, NarakaMod.location(name), blockItem);
        return block;
    }

    private static <B extends Block> B registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(name, function, properties, item());
    }

    private static <B extends Block> B registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase, Item.Properties itemProperties) {
        return registerBlockWithItem(name, function, from(propertyBase), itemProperties);
    }

    private static <B extends Block> B registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        return registerBlockWithItem(name, function, BlockBehaviour.Properties.ofFullCopy(propertyBase));
    }

    private static Block registerSimpleBlockWithItem(String name, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        return registerBlockWithItem(name, Block::new, blockProperties, itemProperties);
    }

    private static Block registerSimpleBlockWithItem(String name, Block block, Item.Properties itemProperties) {
        return registerBlockWithItem(name, Block::new, from(block), itemProperties);
    }

    private static Block registerSimpleBlockWithItem(String name, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(name, Block::new, properties);
    }

    private static Block registerSimpleBlockWithItem(String name, Block propertyBase) {
        return registerBlockWithItem(name, Block::new, BlockBehaviour.Properties.ofFullCopy(propertyBase));
    }

    public static void initialize() {

    }
}

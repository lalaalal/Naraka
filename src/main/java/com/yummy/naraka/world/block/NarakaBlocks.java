package com.yummy.naraka.world.block;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class NarakaBlocks {
    public static final String SOUL_INFUSED_PREFIX = "soul_infused_";

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
    public static final DeferredBlock<Block> NECTARIUM_BLOCK = registerBlockWithItem("nectarium_block", NectariumBlock::new, Blocks.IRON_BLOCK);
    public static final DeferredBlock<PurifiedSoulBlock> PURIFIED_SOUL_BLOCK = registerBlockWithItem(
            "purified_soul_block",
            PurifiedSoulBlock::new,
            from(Blocks.SOUL_SAND).mapColor(DyeColor.WHITE),
            item().fireResistant()
    );
    public static final DeferredBlock<Block> PURIFIED_SOUL_METAL_BLOCK = registerSimpleBlockWithItem(
            "purified_soul_metal_block",
            from(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK),
            item().fireResistant()
    );
    public static final DeferredBlock<BaseFireBlock> PURIFIED_SOUL_FIRE_BLOCK = registerBlockWithItem(
            "purified_soul_fire",
            PurifiedSoulFireBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_FIRE)
                    .noLootTable()
                    .lightLevel(state -> 7)
                    .mapColor(MapColor.COLOR_BLACK),
            new Item.Properties()
    );
    public static final DeferredBlock<SoulCraftingBlock> SOUL_CRAFTING_BLOCK = registerBlockWithItem(
            "soul_crafting_block",
            SoulCraftingBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BLAST_FURNACE)
                    .lightLevel(SoulCraftingBlock::lightLevel)
    );

    public static final DeferredBlock<Block> COMPRESSED_IRON_BLOCK = registerSimpleBlockWithItem("compressed_iron_block", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> FAKE_GOLD_BLOCK = registerSimpleBlockWithItem("fake_gold_block", from(Blocks.GOLD_BLOCK).strength(5, 6));
    public static final DeferredBlock<Block> AMETHYST_SHARD_BLOCK = registerSimpleBlockWithItem(
            "amethyst_shard_block",
            from(Blocks.AMETHYST_BLOCK).strength(0.5f)
    );

    public static final DeferredBlock<Block> SOUL_INFUSED_REDSTONE_BLOCK = registerSoulInfusedBlock("redstone_block", Blocks.REDSTONE_BLOCK);
    public static final DeferredBlock<Block> SOUL_INFUSED_COPPER_BLOCK = registerSoulInfusedBlock("copper_block", Blocks.COPPER_BLOCK);
    public static final DeferredBlock<Block> SOUL_INFUSED_GOLD_BLOCK = registerSoulInfusedBlock("gold_block", Blocks.GOLD_BLOCK);
    public static final DeferredBlock<Block> SOUL_INFUSED_EMERALD_BLOCK = registerSoulInfusedBlock("emerald_block", Blocks.EMERALD_BLOCK);
    public static final DeferredBlock<Block> SOUL_INFUSED_DIAMOND_BLOCK = registerSoulInfusedBlock("diamond_block", Blocks.DIAMOND_BLOCK);
    public static final DeferredBlock<Block> SOUL_INFUSED_LAPIS_BLOCK = registerSoulInfusedBlock("lapis_block", Blocks.LAPIS_BLOCK);
    public static final DeferredBlock<Block> SOUL_INFUSED_AMETHYST_BLOCK = registerSoulInfusedBlock("amethyst_block", Blocks.AMETHYST_BLOCK);
    public static final DeferredBlock<Block> SOUL_INFUSED_NECTARIUM_BLOCK = registerSoulInfusedBlock("nectarium_block", NECTARIUM_BLOCK);

    public static final DeferredBlock<RotatedPillarBlock> EBONY_LOG = registerBlockWithItem("ebony_log", RotatedPillarBlock::new, Blocks.DARK_OAK_LOG);
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_EBONY_LOG = registerBlockWithItem("stripped_ebony_log", RotatedPillarBlock::new, Blocks.STRIPPED_DARK_OAK_LOG);
    public static final DeferredBlock<RotatedPillarBlock> EBONY_WOOD = registerBlockWithItem("ebony_wood", RotatedPillarBlock::new, Blocks.DARK_OAK_WOOD);
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_EBONY_WOOD = registerBlockWithItem("stripped_ebony_wood", RotatedPillarBlock::new, Blocks.STRIPPED_DARK_OAK_WOOD);
    public static final DeferredBlock<LeavesBlock> EBONY_LEAVES = registerBlockWithItem("ebony_leaves", LeavesBlock::new, Blocks.DARK_OAK_LEAVES);

    public static final DeferredBlock<Block> EBONY_PLANKS = registerSimpleBlockWithItem("ebony_planks", Blocks.DARK_OAK_PLANKS);
    public static final DeferredBlock<SlabBlock> EBONY_SLAB = registerBlockWithItem("ebony_slab", SlabBlock::new, Blocks.DARK_OAK_SLAB);
    public static final DeferredBlock<StairBlock> EBONY_STAIRS = registerBlockWithItem("ebony_stairs", properties -> new StairBlock(EBONY_PLANKS.get().defaultBlockState(), properties), Blocks.DARK_OAK_STAIRS);
    public static final DeferredBlock<FenceBlock> EBONY_FENCE = registerBlockWithItem("ebony_fence", FenceBlock::new, Blocks.DARK_OAK_FENCE);
    public static final DeferredBlock<FenceGateBlock> EBONY_FENCE_GATE = registerBlockWithItem("ebony_fence_gate", properties -> new FenceGateBlock(NarakaBlockTypes.Wood.EBONY, properties), Blocks.DARK_OAK_FENCE_GATE);
    public static final DeferredBlock<DoorBlock> EBONY_DOOR = registerBlockWithItem("ebony_door", properties -> new DoorBlock(NarakaBlockTypes.BlockSet.EBONY, properties), Blocks.DARK_OAK_DOOR);
    public static final DeferredBlock<TrapDoorBlock> EBONY_TRAPDOOR = registerBlockWithItem("ebony_trapdoor", properties -> new TrapDoorBlock(NarakaBlockTypes.BlockSet.EBONY, properties), Blocks.DARK_OAK_TRAPDOOR);
    public static final DeferredBlock<PressurePlateBlock> EBONY_PRESSURE_PLATE = registerBlockWithItem("ebony_pressure_plate", properties -> new PressurePlateBlock(NarakaBlockTypes.BlockSet.EBONY, properties), Blocks.DARK_OAK_PRESSURE_PLATE);
    public static final DeferredBlock<ButtonBlock> EBONY_BUTTON = registerBlockWithItem("ebony_block", properties -> new ButtonBlock(NarakaBlockTypes.BlockSet.EBONY, 30, properties), Blocks.DARK_OAK_BUTTON);

    public static final DeferredBlock<NarakaStandingSignBlock> EBONY_SIGN = registerBlock(
            "ebony_sign",
            properties -> new NarakaStandingSignBlock(NarakaBlockTypes.Wood.EBONY, properties),
            Blocks.DARK_OAK_SIGN
    );
    public static final DeferredBlock<NarakaWallSignBlock> EBONY_WALL_SIGN = registerBlock(
            "ebony_wall_sign",
            properties -> new NarakaWallSignBlock(NarakaBlockTypes.Wood.EBONY, properties),
            Blocks.DARK_OAK_WALL_SIGN
    );
    public static final DeferredBlock<NarakaCeilingHangingSignBlock> EBONY_HANGING_SIGN = registerBlock(
            "ebony_hanging_sign",
            properties -> new NarakaCeilingHangingSignBlock(NarakaBlockTypes.Wood.EBONY, properties),
            Blocks.DARK_OAK_HANGING_SIGN
    );
    public static final DeferredBlock<NarakaWallHangingSignBlock> EBONY_WALL_HANGING_SIGN = registerBlock(
            "ebony_wall_hanging_sign",
            properties -> new NarakaWallHangingSignBlock(NarakaBlockTypes.Wood.EBONY, properties),
            Blocks.DARK_OAK_WALL_HANGING_SIGN
    );
    public static final DeferredBlock<EbonySaplingBlock> EBONY_SAPLING = registerBlockWithItem("ebony_sapling", EbonySaplingBlock::new, Blocks.DARK_OAK_SAPLING);
    public static final DeferredBlock<Block> HARD_EBONY_PLANKS = registerBlockWithItem(
            "hard_ebony_planks",
            properties -> new RotatedPillarBlock(properties.strength(40, 6)),
            Blocks.DARK_OAK_PLANKS
    );

    public static final DeferredBlock<HerobrineTotem> HEROBRINE_TOTEM = registerBlockWithItem(
            "herobrine_totem",
            properties -> new HerobrineTotem(properties
                    .pushReaction(PushReaction.BLOCK)
                    .strength(50, 1200)
                    .noLootTable()
                    .lightLevel(HerobrineTotem::light)
            ),
            Blocks.NETHER_BRICKS
    );


    public static final List<DeferredBlock<Block>> SOUL_INFUSED_BLOCKS = List.of(
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
        for (DeferredBlock<Block> soulInfusedBlock : SOUL_INFUSED_BLOCKS)
            consumer.accept(soulInfusedBlock.get());
    }

    public static void forEachSoulInfusedBlockHolder(Consumer<DeferredBlock<Block>> consumer) {
        for (DeferredBlock<Block> soulInfusedBlock : SOUL_INFUSED_BLOCKS)
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
        fire.setFlammable(EBONY_LOG.get(), 5, 5);
        fire.setFlammable(STRIPPED_EBONY_LOG.get(), 5, 5);
        fire.setFlammable(EBONY_WOOD.get(), 5, 5);
        fire.setFlammable(STRIPPED_EBONY_WOOD.get(), 5, 5);
        fire.setFlammable(EBONY_LEAVES.get(), 30, 60);
        fire.setFlammable(EBONY_PLANKS.get(), 5, 20);
        fire.setFlammable(EBONY_SLAB.get(), 5, 20);
        fire.setFlammable(EBONY_STAIRS.get(), 5, 20);
    }

    private static DeferredBlock<Block> registerSoulInfusedBlock(String name, DeferredBlock<Block> baseBlock) {
        return registerSimpleBlockWithItem(SOUL_INFUSED_PREFIX + name, baseBlock, item().fireResistant());
    }

    private static DeferredBlock<Block> registerSoulInfusedBlock(String name, Block baseBlock) {
        return registerSimpleBlockWithItem(SOUL_INFUSED_PREFIX + name, baseBlock, item().fireResistant());
    }


    private static <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        return BLOCKS.registerBlock(name, function, BlockBehaviour.Properties.ofFullCopy(propertyBase));
    }

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

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, DeferredBlock<? extends Block> propertyBase) {
        DeferredBlock<B> blockHolder = BLOCKS.register(name, properties -> function.apply(BlockBehaviour.Properties.ofFullCopy(propertyBase.get())));
        ITEMS.registerSimpleBlockItem(blockHolder);
        return blockHolder;
    }

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, DeferredBlock<? extends Block> propertyBase, Item.Properties itemProperties) {
        DeferredBlock<B> blockHolder = BLOCKS.register(name, properties -> function.apply(BlockBehaviour.Properties.ofFullCopy(propertyBase.get())));
        ITEMS.registerSimpleBlockItem(blockHolder, itemProperties);
        return blockHolder;
    }

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        return registerBlockWithItem(name, function, BlockBehaviour.Properties.ofFullCopy(propertyBase));
    }

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase, Item.Properties itemProperties) {
        return registerBlockWithItem(name, function, BlockBehaviour.Properties.ofFullCopy(propertyBase), itemProperties);
    }

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function) {
        return registerBlockWithItem(name, function, BlockBehaviour.Properties.of());
    }

    private static DeferredBlock<Block> registerSimpleBlockWithItem(String name, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        return registerBlockWithItem(name, Block::new, blockProperties, itemProperties);
    }

    private static DeferredBlock<Block> registerSimpleBlockWithItem(String name, Block block, Item.Properties itemProperties) {
        return registerBlockWithItem(name, Block::new, from(block), itemProperties);
    }

    private static DeferredBlock<Block> registerSimpleBlockWithItem(String name, DeferredBlock<? extends Block> propertyBase) {
        return registerBlockWithItem(name, Block::new, propertyBase);
    }

    private static DeferredBlock<Block> registerSimpleBlockWithItem(String name, DeferredBlock<? extends Block> propertyBase, Item.Properties itemProperties) {
        return registerBlockWithItem(name, Block::new, propertyBase, itemProperties);
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

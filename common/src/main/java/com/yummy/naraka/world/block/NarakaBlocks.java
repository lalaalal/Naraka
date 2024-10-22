package com.yummy.naraka.world.block;

import com.yummy.naraka.NarakaMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
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
import java.util.function.Supplier;

public class NarakaBlocks {
    public static final String SOUL_INFUSED_PREFIX = "soul_infused_";

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(NarakaMod.MOD_ID, Registries.BLOCK);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(NarakaMod.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<TransparentBlock> TRANSPARENT_BLOCK = registerBlockWithItem(
            "transparent_block",
            properties -> new TransparentBlock(
                    properties
                            .noCollission()
                            .forceSolidOn()
                            .noLootTable()
            ),
            Blocks.BEDROCK
    );

    public static final RegistrySupplier<DropExperienceBlock> NECTARIUM_ORE = registerBlockWithItem(
            "nectarium_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            Blocks.IRON_ORE
    );
    public static final RegistrySupplier<DropExperienceBlock> DEEPSLATE_NECTARIUM_ORE = registerBlockWithItem(
            "deepslate_nectarium_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            Blocks.DEEPSLATE_IRON_ORE
    );
    public static final RegistrySupplier<Block> NECTARIUM_BLOCK = registerBlockWithItem(
            "nectarium_block",
            properties -> new EncroachingBlock(properties, Blocks.HONEY_BLOCK),
            Blocks.IRON_BLOCK
    );

    public static final RegistrySupplier<NectariumCoreBlock> NECTARIUM_CORE_BLOCK = registerBlockWithItem(
            "nectarium_core",
            properties -> new NectariumCoreBlock(properties
                    .requiresCorrectToolForDrops()
                    .lightLevel(NectariumCoreBlock::lightLevel)),
            Blocks.AMETHYST_CLUSTER
    );
    public static final RegistrySupplier<NectariumCrystalBlock> NECTARIUM_CRYSTAL_BLOCK = registerBlockWithItem(
            "nectarium_crystal",
            properties -> new NectariumCrystalBlock(properties
                    .requiresCorrectToolForDrops()),
            Blocks.AMETHYST_BLOCK
    );

    public static final RegistrySupplier<PurifiedSoulBlock> PURIFIED_SOUL_BLOCK = registerBlockWithItem(
            "purified_soul_block",
            PurifiedSoulBlock::new,
            from(Blocks.SOUL_SAND)
                    .mapColor(DyeColor.WHITE)
                    .requiresCorrectToolForDrops(),
            item().fireResistant()
    );
    public static final RegistrySupplier<Block> PURIFIED_SOUL_METAL_BLOCK = registerSimpleBlockWithItem(
            "purified_soul_metal_block",
            from(Blocks.IRON_BLOCK)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .requiresCorrectToolForDrops(),
            item().fireResistant()
    );
    public static final RegistrySupplier<BaseFireBlock> PURIFIED_SOUL_FIRE_BLOCK = registerBlockWithItem(
            "purified_soul_fire",
            PurifiedSoulFireBlock::new,
            from(Blocks.SOUL_FIRE)
                    .mapColor(MapColor.COLOR_BLACK)
                    .noLootTable()
                    .lightLevel(state -> 7)

    );
    public static final RegistrySupplier<SoulCraftingBlock> SOUL_CRAFTING_BLOCK = registerBlockWithItem(
            "soul_crafting_block",
            SoulCraftingBlock::new,
            from(Blocks.BLAST_FURNACE)
                    .lightLevel(SoulCraftingBlock::lightLevel)
    );
    public static final RegistrySupplier<ForgingBlock> FORGING_BLOCK = registerBlockWithItem(
            "forging_block",
            ForgingBlock::new,
            Blocks.ANVIL
    );
    public static final RegistrySupplier<SoulSmithingBlock> SOUL_SMITHING_BLOCK = registerBlockWithItem(
            "soul_smithing_block",
            SoulSmithingBlock::new,
            Blocks.ANVIL
    );

    public static final RegistrySupplier<Block> COMPRESSED_IRON_BLOCK = registerSimpleBlockWithItem("compressed_iron_block", Blocks.IRON_BLOCK);
    public static final RegistrySupplier<Block> IMITATION_GOLD_BLOCK = registerBlockWithItem(
            "imitation_gold_block",
            properties -> new EncroachingBlock(properties.strength(5, 6), Blocks.IRON_BLOCK),
            Blocks.GOLD_BLOCK
    );
    public static final RegistrySupplier<Block> AMETHYST_SHARD_BLOCK = registerSimpleBlockWithItem(
            "amethyst_shard_block",
            from(Blocks.AMETHYST_BLOCK)
                    .strength(0.5f)
                    .requiresCorrectToolForDrops()
    );

    public static final RegistrySupplier<Block> SOUL_INFUSED_REDSTONE_BLOCK = registerSoulInfusedBlock("redstone_block", Blocks.REDSTONE_BLOCK);
    public static final RegistrySupplier<Block> SOUL_INFUSED_COPPER_BLOCK = registerSoulInfusedBlock("copper_block", Blocks.COPPER_BLOCK);
    public static final RegistrySupplier<Block> SOUL_INFUSED_GOLD_BLOCK = registerSoulInfusedBlock("gold_block", Blocks.GOLD_BLOCK);
    public static final RegistrySupplier<Block> SOUL_INFUSED_EMERALD_BLOCK = registerSoulInfusedBlock("emerald_block", Blocks.EMERALD_BLOCK);
    public static final RegistrySupplier<Block> SOUL_INFUSED_DIAMOND_BLOCK = registerSoulInfusedBlock("diamond_block", Blocks.DIAMOND_BLOCK);
    public static final RegistrySupplier<Block> SOUL_INFUSED_LAPIS_BLOCK = registerSoulInfusedBlock("lapis_block", Blocks.LAPIS_BLOCK);
    public static final RegistrySupplier<Block> SOUL_INFUSED_AMETHYST_BLOCK = registerSoulInfusedBlock("amethyst_block", Blocks.AMETHYST_BLOCK);
    public static final RegistrySupplier<Block> SOUL_INFUSED_NECTARIUM_BLOCK = registerSoulInfusedBlock("nectarium_block", NECTARIUM_BLOCK);

    public static final RegistrySupplier<EbonyLogBlock> EBONY_LOG = registerBlockWithItem("ebony_log", EbonyLogBlock::new,
            from(Blocks.DARK_OAK_LOG)
                    .strength(5, 6)
                    .requiresCorrectToolForDrops()
    );
    public static final RegistrySupplier<RotatedPillarBlock> STRIPPED_EBONY_LOG = registerBlockWithItem("stripped_ebony_log", RotatedPillarBlock::new,
            from(Blocks.STRIPPED_DARK_OAK_LOG)
                    .strength(5, 6).
                    requiresCorrectToolForDrops()
    );
    public static final RegistrySupplier<RotatedPillarBlock> EBONY_WOOD = registerBlockWithItem("ebony_wood", RotatedPillarBlock::new,
            from(Blocks.DARK_OAK_WOOD)
                    .strength(5, 6).
                    requiresCorrectToolForDrops()
    );
    public static final RegistrySupplier<RotatedPillarBlock> STRIPPED_EBONY_WOOD = registerBlockWithItem("stripped_ebony_wood", RotatedPillarBlock::new,
            from(Blocks.STRIPPED_DARK_OAK_WOOD)
                    .strength(5, 6).
                    requiresCorrectToolForDrops()
    );
    public static final RegistrySupplier<Block> HARD_EBONY_PLANKS = registerBlockWithItem("hard_ebony_planks", RotatedPillarBlock::new,
            from(Blocks.DARK_OAK_PLANKS)
                    .strength(40, 6)
                    .requiresCorrectToolForDrops()
    );
    public static final RegistrySupplier<Block> EBONY_METAL_BLOCK = registerSimpleBlockWithItem("ebony_metal_block", Blocks.NETHERITE_BLOCK);
    public static final RegistrySupplier<EbonyLeavesBlock> EBONY_LEAVES = registerBlockWithItem("ebony_leaves", EbonyLeavesBlock::new, Blocks.DARK_OAK_LEAVES);
    public static final RegistrySupplier<MangroveRootsBlock> EBONY_ROOTS = registerBlockWithItem("ebony_roots", MangroveRootsBlock::new, Blocks.MANGROVE_ROOTS);

    public static final RegistrySupplier<EbonySaplingBlock> EBONY_SAPLING = registerBlockWithItem("ebony_sapling", EbonySaplingBlock::new, Blocks.DARK_OAK_SAPLING);
    public static final RegistrySupplier<FlowerPotBlock> POTTED_EBONY_SAPLING = registerBlockWithItem("potted_ebony_sapling", properties -> new FlowerPotBlock(EBONY_SAPLING.get(), properties), Blocks.POTTED_DARK_OAK_SAPLING);

    public static final RegistrySupplier<HerobrineTotem> HEROBRINE_TOTEM = registerBlockWithItem(
            "herobrine_totem",
            properties -> new HerobrineTotem(properties
                    .pushReaction(PushReaction.BLOCK)
                    .strength(50, 1200)
                    .requiresCorrectToolForDrops()
                    .lightLevel(HerobrineTotem::light)
            ),
            Blocks.NETHER_BRICKS
    );

    public static final RegistrySupplier<NarakaForgingBlock> NARAKA_FORGING_BLOCK = registerBlockWithItem(
            "naraka_forging_block",
            NarakaForgingBlock::new,
            Blocks.ANVIL
    );

    public static final RegistrySupplier<SoulStabilizer> SOUL_STABILIZER = registerBlockWithItem(
            "soul_stabilizer",
            SoulStabilizer::new,
            Blocks.GLASS
    );

    public static final List<Supplier<Block>> SOUL_INFUSED_BLOCKS = List.of(
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
        for (Supplier<Block> soulInfusedBlock : SOUL_INFUSED_BLOCKS)
            consumer.accept(soulInfusedBlock.get());
    }


    private static BlockBehaviour.Properties from(Block block) {
        return BlockBehaviour.Properties.ofFullCopy(block);
    }

    private static Item.Properties item() {
        return new Item.Properties();
    }

    private static RegistrySupplier<Block> registerSoulInfusedBlock(String name, Block baseBlock) {
        return registerSimpleBlockWithItem(SOUL_INFUSED_PREFIX + name, baseBlock, item().fireResistant());
    }

    private static RegistrySupplier<Block> registerSoulInfusedBlock(String name, Supplier<Block> baseBlock) {
        return registerBlockWithItem(SOUL_INFUSED_PREFIX + name, Block::new, baseBlock, item().fireResistant());
    }

    private static <B extends Block> RegistrySupplier<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(propertyBase);
        return BLOCKS.register(name, () -> function.apply(properties));
    }

    private static <B extends Block> RegistrySupplier<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        RegistrySupplier<B> block = BLOCKS.register(name, () -> function.apply(blockProperties));
        ITEMS.register(name, () -> new BlockItem(block.get(), itemProperties));
        return block;
    }

    private static <B extends Block> RegistrySupplier<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Supplier<Block> blockSupplier, Item.Properties itemProperties) {
        RegistrySupplier<B> block = BLOCKS.register(name, () -> function.apply(from(blockSupplier.get())));
        ITEMS.register(name, () -> new BlockItem(block.get(), itemProperties));
        return block;
    }

    private static <B extends Block> RegistrySupplier<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(name, function, properties, item());
    }

    private static <B extends Block> RegistrySupplier<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase, Item.Properties itemProperties) {
        return registerBlockWithItem(name, function, from(propertyBase), itemProperties);
    }

    private static <B extends Block> RegistrySupplier<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        return registerBlockWithItem(name, function, BlockBehaviour.Properties.ofFullCopy(propertyBase));
    }

    private static RegistrySupplier<Block> registerSimpleBlockWithItem(String name, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        return registerBlockWithItem(name, Block::new, blockProperties, itemProperties);
    }

    private static RegistrySupplier<Block> registerSimpleBlockWithItem(String name, Block block, Item.Properties itemProperties) {
        return registerBlockWithItem(name, Block::new, from(block), itemProperties);
    }

    private static RegistrySupplier<Block> registerSimpleBlockWithItem(String name, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(name, Block::new, properties);
    }

    private static RegistrySupplier<Block> registerSimpleBlockWithItem(String name, Block propertyBase) {
        return registerBlockWithItem(name, Block::new, BlockBehaviour.Properties.ofFullCopy(propertyBase));
    }

    public static void initialize() {
        BLOCKS.register();
        ITEMS.register();
    }
}

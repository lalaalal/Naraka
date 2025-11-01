package com.yummy.naraka.world.block;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NarakaBlocks {
    public static final String SOUL_INFUSED_PREFIX = "soul_infused_";

    public static final HolderProxy<Block, TransparentBlock> TRANSPARENT_BLOCK = registerBlockWithItem(
            "transparent_block",
            properties -> new TransparentBlock(
                    properties.forceSolidOn()
                            .noTerrainParticles()
                            .noLootTable()
            ),
            Blocks.BEDROCK
    );

    public static final HolderProxy<Block, DiamondGolemSpawner> DIAMOND_GOLEM_SPAWNER = registerBlock(
            "diamond_golem_spawner",
            properties -> new DiamondGolemSpawner(
                    properties.noLootTable()
                            .noTerrainParticles()
                            .noCollision()
            ),
            Blocks.SPAWNER
    );

    public static final HolderProxy<Block, DropExperienceBlock> AMETHYST_ORE = registerBlockWithItem(
            "amethyst_ore",
            properties -> new DropExperienceBlock(UniformInt.of(0, 2), properties.requiresCorrectToolForDrops()),
            Blocks.STONE
    );
    public static final HolderProxy<Block, DropExperienceBlock> DEEPSLATE_AMETHYST_ORE = registerBlockWithItem(
            "deepslate_amethyst_ore",
            properties -> new DropExperienceBlock(UniformInt.of(1, 2), properties.requiresCorrectToolForDrops()),
            Blocks.DEEPSLATE
    );

    public static final HolderProxy<Block, DropExperienceBlock> NECTARIUM_ORE = registerBlockWithItem(
            "nectarium_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            Blocks.IRON_ORE
    );
    public static final HolderProxy<Block, DropExperienceBlock> DEEPSLATE_NECTARIUM_ORE = registerBlockWithItem(
            "deepslate_nectarium_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            Blocks.DEEPSLATE_IRON_ORE
    );
    public static final HolderProxy<Block, Block> NECTARIUM_BLOCK = registerBlockWithItem(
            "nectarium_block",
            properties -> new EncroachingBlock(properties, Blocks.HONEY_BLOCK),
            Blocks.IRON_BLOCK
    );

    public static final HolderProxy<Block, NectariumCoreBlock> NECTARIUM_CORE_BLOCK = registerBlockWithItem(
            "nectarium_core",
            properties -> new NectariumCoreBlock(properties
                    .requiresCorrectToolForDrops()
                    .lightLevel(NectariumCoreBlock::lightLevel)),
            Blocks.AMETHYST_CLUSTER
    );
    public static final HolderProxy<Block, NectariumCrystalBlock> NECTARIUM_CRYSTAL_BLOCK = registerBlockWithItem(
            "nectarium_crystal",
            properties -> new NectariumCrystalBlock(properties
                    .requiresCorrectToolForDrops()),
            Blocks.AMETHYST_BLOCK
    );

    public static final HolderProxy<Block, Block> PURIFIED_SOUL_LAMP = registerSimpleBlockWithItem(
            "purified_soul_lamp",
            from(Blocks.REDSTONE_LAMP)
                    .lightLevel(state -> 15)
                    .emissiveRendering(NarakaBlocks::always)
    );

    public static final HolderProxy<Block, Block> PURIFIED_SOUL_LANTERN = registerSimpleBlockWithItem(
            "purified_soul_lantern",
            from(Blocks.SEA_LANTERN)
                    .lightLevel(state -> 15)
                    .emissiveRendering(NarakaBlocks::always)
    );

    public static final HolderProxy<Block, PurifiedSoulBlock> PURIFIED_SOUL_BLOCK = registerBlockWithItem(
            "purified_soul_block",
            PurifiedSoulBlock::new,
            from(Blocks.SOUL_SAND)
                    .mapColor(DyeColor.WHITE)
                    .requiresCorrectToolForDrops(),
            item().fireResistant()
    );
    public static final HolderProxy<Block, Block> PURIFIED_SOUL_METAL_BLOCK = registerSimpleBlockWithItem(
            "purified_soul_metal_block",
            from(Blocks.IRON_BLOCK)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .requiresCorrectToolForDrops(),
            item().fireResistant()
    );
    public static final HolderProxy<Block, BaseFireBlock> PURIFIED_SOUL_FIRE_BLOCK = registerBlock(
            "purified_soul_fire",
            PurifiedSoulFireBlock::new,
            from(Blocks.SOUL_FIRE)
                    .mapColor(MapColor.COLOR_BLACK)
                    .noLootTable()
                    .lightLevel(state -> 7)
    );

    public static final HolderProxy<Block, SoulSmithingBlock> SOUL_SMITHING_BLOCK = registerBlockWithItem(
            "soul_smithing_block",
            SoulSmithingBlock::new,
            Blocks.SMITHING_TABLE
    );

    public static final HolderProxy<Block, Block> IMITATION_GOLD_BLOCK = registerBlockWithItem(
            "imitation_gold_block",
            properties -> new EncroachingBlock(properties.strength(5, 6), Blocks.IRON_BLOCK),
            Blocks.GOLD_BLOCK
    );
    public static final HolderProxy<Block, Block> AMETHYST_SHARD_BLOCK = registerSimpleBlockWithItem(
            "amethyst_shard_block",
            from(Blocks.AMETHYST_BLOCK)
                    .strength(0.5f)
                    .requiresCorrectToolForDrops()
    );

    public static final HolderProxy<Block, Block> SOUL_INFUSED_REDSTONE_BLOCK = registerSoulInfusedBlock("redstone_block", Blocks.REDSTONE_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_COPPER_BLOCK = registerSoulInfusedBlock("copper_block", Blocks.COPPER_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_GOLD_BLOCK = registerSoulInfusedBlock("gold_block", Blocks.GOLD_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_EMERALD_BLOCK = registerSoulInfusedBlock("emerald_block", Blocks.EMERALD_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_DIAMOND_BLOCK = registerSoulInfusedBlock("diamond_block", Blocks.DIAMOND_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_LAPIS_BLOCK = registerSoulInfusedBlock("lapis_block", Blocks.LAPIS_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_AMETHYST_BLOCK = registerSoulInfusedBlock("amethyst_block", Blocks.AMETHYST_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_NECTARIUM_BLOCK = registerSoulInfusedBlock("nectarium_block", NECTARIUM_BLOCK);

    public static final HolderProxy<Block, HerobrineTotem> HEROBRINE_TOTEM = registerBlockWithItem(
            "herobrine_totem",
            properties -> new HerobrineTotem(properties
                    .pushReaction(PushReaction.BLOCK)
                    .strength(50, 1200)
                    .requiresCorrectToolForDrops()
                    .lightLevel(HerobrineTotem::light)
            ),
            Blocks.NETHER_BRICKS
    );

    public static final HolderProxy<Block, SoulStabilizer> SOUL_STABILIZER = registerBlockWithItem(
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

    private static BlockBehaviour.Properties from(String name, Block block) {
        return from(block).setId(NarakaBlocks.key(name));
    }

    private static Item.Properties item() {
        return new Item.Properties();
    }

    private static HolderProxy<Block, Block> registerSoulInfusedBlock(String name, Block baseBlock) {
        return registerSimpleBlockWithItem(SOUL_INFUSED_PREFIX + name, baseBlock, item().fireResistant());
    }

    private static HolderProxy<Block, Block> registerSoulInfusedBlock(String name, Supplier<Block> baseBlock) {
        return registerBlockWithItem(SOUL_INFUSED_PREFIX + name, Block::new, baseBlock, item().fireResistant());
    }

    public static ResourceKey<Block> key(String name) {
        return ResourceKey.create(Registries.BLOCK, NarakaMod.location(name));
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(propertyBase)
                .setId(NarakaBlocks.key(name));
        return RegistryProxy.register(Registries.BLOCK, name, () -> function.apply(properties));
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties properties) {
        return RegistryProxy.register(Registries.BLOCK, name, () -> function.apply(properties.setId(NarakaBlocks.key(name))));
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        blockProperties.setId(NarakaBlocks.key(name));
        itemProperties.setId(NarakaItems.key(name));
        HolderProxy<Block, B> block = RegistryProxy.register(Registries.BLOCK, name, () -> function.apply(blockProperties));
        RegistryProxy.register(Registries.ITEM, name, () -> new BlockItem(block.get(), itemProperties));
        return block;
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Supplier<Block> blockSupplier, Item.Properties itemProperties) {
        itemProperties.setId(NarakaItems.key(name));
        HolderProxy<Block, B> block = RegistryProxy.register(Registries.BLOCK, name,
                () -> function.apply(
                        from(name, blockSupplier.get())
                )
        );
        RegistryProxy.register(Registries.ITEM, name, () -> new BlockItem(block.get(), itemProperties));
        return block;
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(name, function, properties, item());
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase, Item.Properties itemProperties) {
        return registerBlockWithItem(name, function, from(propertyBase), itemProperties);
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        return registerBlockWithItem(name, function, BlockBehaviour.Properties.ofFullCopy(propertyBase));
    }

    private static HolderProxy<Block, Block> registerSimpleBlockWithItem(String name, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        return registerBlockWithItem(name, Block::new, blockProperties, itemProperties);
    }

    private static HolderProxy<Block, Block> registerSimpleBlockWithItem(String name, Block block, Item.Properties itemProperties) {
        return registerBlockWithItem(name, Block::new, from(block), itemProperties);
    }

    private static HolderProxy<Block, Block> registerSimpleBlockWithItem(String name, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(name, Block::new, properties);
    }

    private static HolderProxy<Block, Block> registerSimpleBlockWithItem(String name, Block propertyBase) {
        return registerBlockWithItem(name, Block::new, from(propertyBase));
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    public static void initialize() {

    }
}

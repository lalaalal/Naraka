package com.yummy.naraka.world.block;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryWriter;
import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.references.BlockItemId;
import com.yummy.naraka.references.NarakaBlockIds;
import com.yummy.naraka.references.NarakaBlockItemIds;
import com.yummy.naraka.world.item.DefaultItemTagBuilder;
import com.yummy.naraka.world.item.NarakaItemTooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class NarakaBlocks {
    public static final HolderProxy<Block, TransparentBlock> TRANSPARENT_BLOCK = registerBlockWithItem(
            NarakaBlockItemIds.TRANSPARENT_BLOCK,
            TransparentBlock::new,
            from(Blocks.BEDROCK)
                    .forceSolidOn()
                    .noParticlesOnBreak()
                    .noLootTable()
    );

    public static final HolderProxy<Block, DiamondGolemSpawner> DIAMOND_GOLEM_SPAWNER = registerBlock(
            NarakaBlockIds.DIAMOND_GOLEM_SPAWNER,
            DiamondGolemSpawner::new,
            from(Blocks.SPAWNER)
                    .noLootTable()
                    .noParticlesOnBreak()
                    .noCollission()
    );

    public static final HolderProxy<Block, DropExperienceBlock> AMETHYST_ORE = registerBlockWithItem(
            NarakaBlockItemIds.AMETHYST_ORE,
            properties -> new DropExperienceBlock(properties, UniformInt.of(0, 2)),
            from(Blocks.STONE).requiresCorrectToolForDrops()
    );
    public static final HolderProxy<Block, DropExperienceBlock> DEEPSLATE_AMETHYST_ORE = registerBlockWithItem(
            NarakaBlockItemIds.DEEPSLATE_AMETHYST_ORE,
            properties -> new DropExperienceBlock(properties, UniformInt.of(1, 2)),
            from(Blocks.DEEPSLATE).requiresCorrectToolForDrops()
    );

    public static final HolderProxy<Block, DropExperienceBlock> NECTARIUM_ORE = registerBlockWithItem(
            NarakaBlockItemIds.NECTARIUM_ORE,
            properties -> new DropExperienceBlock(properties, UniformInt.of(3, 7)),
            Blocks.IRON_ORE
    );
    public static final HolderProxy<Block, DropExperienceBlock> DEEPSLATE_NECTARIUM_ORE = registerBlockWithItem(
            NarakaBlockItemIds.DEEPSLATE_NECTARIUM_ORE,
            properties -> new DropExperienceBlock(properties, UniformInt.of(3, 7)),
            Blocks.DEEPSLATE_IRON_ORE
    );
    public static final HolderProxy<Block, Block> NECTARIUM_BLOCK = registerBlockWithItem(
            NarakaBlockItemIds.NECTARIUM_BLOCK,
            properties -> new EncroachingBlock(properties, Blocks.HONEY_BLOCK),
            Blocks.IRON_BLOCK
    );

    public static final HolderProxy<Block, NectariumCoreBlock> NECTARIUM_CORE_BLOCK = registerBlockWithItem(
            NarakaBlockItemIds.NECTARIUM_CORE_BLOCK,
            NectariumCoreBlock::new,
            from(Blocks.AMETHYST_CLUSTER)
                    .requiresCorrectToolForDrops()
                    .lightLevel(NectariumCoreBlock::lightLevel),
            tagBuilder().naraka$tooltip(NarakaItemTooltip.NECTARIUM_CORE)
                    .naraka$asItemProperties()
                    .rarity(Rarity.RARE)
    );
    public static final HolderProxy<Block, NectariumCrystalBlock> NECTARIUM_CRYSTAL_BLOCK = registerBlockWithItem(
            NarakaBlockItemIds.NECTARIUM_CRYSTAL_BLOCK,
            NectariumCrystalBlock::new,
            from(Blocks.AMETHYST_BLOCK).requiresCorrectToolForDrops()
    );

    public static final HolderProxy<Block, Block> PURIFIED_SOUL_LAMP = registerSimpleBlockWithItem(
            NarakaBlockItemIds.PURIFIED_SOUL_LAMP,
            from(Blocks.REDSTONE_LAMP)
                    .lightLevel(state -> 15)
                    .emissiveRendering(NarakaBlocks::always)
    );

    public static final HolderProxy<Block, Block> PURIFIED_SOUL_LANTERN = registerSimpleBlockWithItem(
            NarakaBlockItemIds.PURIFIED_SOUL_LANTERN,
            from(Blocks.SEA_LANTERN)
                    .lightLevel(state -> 15)
                    .emissiveRendering(NarakaBlocks::always)
    );

    public static final HolderProxy<Block, Block> PURIFIED_SOUL_METAL_BLOCK = registerSimpleBlockWithItem(
            NarakaBlockItemIds.PURIFIED_SOUL_METAL_BLOCK,
            from(Blocks.IRON_BLOCK)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .requiresCorrectToolForDrops(),
            item().fireResistant()
    );

    public static final HolderProxy<Block, BaseFireBlock> PURIFIED_SOUL_FIRE_BLOCK = registerBlock(
            NarakaBlockIds.PURIFIED_SOUL_FIRE_BLOCK,
            PurifiedSoulFireBlock::new,
            from(Blocks.SOUL_FIRE)
                    .mapColor(MapColor.COLOR_BLACK)
                    .noLootTable()
                    .lightLevel(state -> 7)
    );

    public static final HolderProxy<Block, SoulSmithingBlock> SOUL_SMITHING_BLOCK = registerBlockWithItem(
            NarakaBlockItemIds.SOUL_SMITHING_BLOCK,
            SoulSmithingBlock::new,
            from(Blocks.SMITHING_TABLE),
            tagBuilder().naraka$tooltip(NarakaItemTooltip.SOUL_SMITHING_BLOCK)
                    .naraka$asItemProperties()
                    .rarity(Rarity.EPIC)
    );

    public static final HolderProxy<Block, Block> IMITATION_GOLD_BLOCK = registerBlockWithItem(
            NarakaBlockItemIds.IMITATION_GOLD_BLOCK,
            properties -> new EncroachingBlock(properties.strength(5, 6), Blocks.IRON_BLOCK),
            Blocks.GOLD_BLOCK,
            tagBuilder().naraka$tooltip(NarakaItemTooltip.IMITATION_GOLD)
                    .naraka$asItemProperties()
                    .rarity(Rarity.UNCOMMON)
    );
    public static final HolderProxy<Block, Block> AMETHYST_SHARD_BLOCK = registerSimpleBlockWithItem(
            NarakaBlockItemIds.AMETHYST_SHARD_BLOCK,
            from(Blocks.AMETHYST_BLOCK)
                    .strength(0.5f)
                    .requiresCorrectToolForDrops()
    );

    public static final HolderProxy<Block, Block> SOUL_INFUSED_REDSTONE_BLOCK = registerSoulInfusedBlock(NarakaBlockItemIds.SOUL_INFUSED_REDSTONE_BLOCK, Blocks.REDSTONE_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_COPPER_BLOCK = registerSoulInfusedBlock(NarakaBlockItemIds.SOUL_INFUSED_COPPER_BLOCK, Blocks.COPPER_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_GOLD_BLOCK = registerSoulInfusedBlock(NarakaBlockItemIds.SOUL_INFUSED_GOLD_BLOCK, Blocks.GOLD_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_EMERALD_BLOCK = registerSoulInfusedBlock(NarakaBlockItemIds.SOUL_INFUSED_EMERALD_BLOCK, Blocks.EMERALD_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_DIAMOND_BLOCK = registerSoulInfusedBlock(NarakaBlockItemIds.SOUL_INFUSED_DIAMOND_BLOCK, Blocks.DIAMOND_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_LAPIS_BLOCK = registerSoulInfusedBlock(NarakaBlockItemIds.SOUL_INFUSED_LAPIS_BLOCK, Blocks.LAPIS_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_AMETHYST_BLOCK = registerSoulInfusedBlock(NarakaBlockItemIds.SOUL_INFUSED_AMETHYST_BLOCK, Blocks.AMETHYST_BLOCK);
    public static final HolderProxy<Block, Block> SOUL_INFUSED_NECTARIUM_BLOCK = registerSoulInfusedBlock(NarakaBlockItemIds.SOUL_INFUSED_NECTARIUM_BLOCK, NECTARIUM_BLOCK);

    public static final HolderProxy<Block, HerobrineTotem> HEROBRINE_TOTEM = registerBlockWithItem(
            NarakaBlockItemIds.HEROBRINE_TOTEM,
            HerobrineTotem::new,
            from(Blocks.NETHER_BRICKS)
                    .pushReaction(PushReaction.BLOCK)
                    .strength(50, 1200)
                    .requiresCorrectToolForDrops()
                    .lightLevel(HerobrineTotem::light),
            tagBuilder().naraka$tooltip(NarakaItemTooltip.HEROBRINE_TOTEM)
                    .naraka$asItemProperties()
                    .rarity(Rarity.EPIC)
    );

    public static final HolderProxy<Block, SoulStabilizer> SOUL_STABILIZER = registerBlockWithItem(
            NarakaBlockItemIds.SOUL_STABILIZER,
            SoulStabilizer::new,
            Blocks.GLASS,
            tagBuilder().naraka$tooltip(NarakaItemTooltip.SOUL_STABILIZER)
                    .naraka$asItemProperties()
    );

    public static final HolderProxy<Block, NarakaPortalBlock> NARAKA_PORTAL = registerBlock(
            NarakaBlockIds.NARAKA_PORTAL,
            NarakaPortalBlock::new,
            from(Blocks.NETHER_PORTAL)
                    .randomTicks()
                    .lightLevel(state -> 15)
                    .mapColor(MapColor.COLOR_BLACK)
                    .pushReaction(PushReaction.BLOCK)
                    .strength(-1, 3600000)
                    .noLootTable()
    );

    public static final List<ValueGetter<Block>> SOUL_INFUSED_BLOCKS = List.of(
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
        for (ValueGetter<Block> soulInfusedBlock : SOUL_INFUSED_BLOCKS)
            consumer.accept(soulInfusedBlock.getConcreteValue());
    }


    private static BlockBehaviour.Properties from(Block block) {
        return BlockBehaviour.Properties.copy(block);
    }

    private static BlockBehaviour.Properties from(String name, Block block) {
        return from(block);
    }

    private static Item.Properties item() {
        return new Item.Properties();
    }

    private static DefaultItemTagBuilder tagBuilder() {
        return (DefaultItemTagBuilder) new Item.Properties();
    }

    private static HolderProxy<Block, Block> registerSoulInfusedBlock(BlockItemId id, Block baseBlock) {
        return registerSimpleBlockWithItem(id, baseBlock, item().fireResistant().rarity(Rarity.RARE));
    }

    private static HolderProxy<Block, Block> registerSoulInfusedBlock(BlockItemId id, ValueGetter<Block> baseBlock) {
        return registerBlockWithItem(id, Block::new, baseBlock, item().fireResistant().rarity(Rarity.RARE));
    }

    public static ResourceKey<Block> key(String name) {
        return ResourceKey.create(Registries.BLOCK, NarakaMod.location(name));
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlock(ResourceKey<Block> key, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(propertyBase);
        return RegistryWriter.register(key, () -> function.apply(properties));
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlock(ResourceKey<Block> key, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties properties) {
        return RegistryWriter.register(key, () -> function.apply(properties));
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        HolderProxy<Block, B> block = RegistryWriter.register(id.block(), () -> function.apply(blockProperties));
        RegistryWriter.register(id.item(), () -> new BlockItem(block.getConcreteValue(), itemProperties));
        return block;
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> function, ValueGetter<Block> blockSupplier, Item.Properties itemProperties) {
        HolderProxy<Block, B> block = RegistryWriter.register(
                id.block(),
                () -> function.apply(
                        from(blockSupplier.getConcreteValue())
                )
        );
        RegistryWriter.register(id.item(), () -> new BlockItem(block.getConcreteValue(), itemProperties));
        return block;
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(id, function, properties, item());
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase, Item.Properties itemProperties) {
        return registerBlockWithItem(id, function, from(propertyBase), itemProperties);
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        return registerBlockWithItem(id, function, BlockBehaviour.Properties.copy(propertyBase));
    }

    private static HolderProxy<Block, Block> registerSimpleBlockWithItem(BlockItemId id, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        return registerBlockWithItem(id, Block::new, blockProperties, itemProperties);
    }

    private static HolderProxy<Block, Block> registerSimpleBlockWithItem(BlockItemId id, Block block, Item.Properties itemProperties) {
        return registerBlockWithItem(id, Block::new, from(block), itemProperties);
    }

    private static HolderProxy<Block, Block> registerSimpleBlockWithItem(BlockItemId id, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(id, Block::new, properties);
    }

    private static HolderProxy<Block, Block> registerSimpleBlockWithItem(BlockItemId id, Block propertyBase) {
        return registerBlockWithItem(id, Block::new, from(propertyBase));
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    public static void initialize() {

    }
}

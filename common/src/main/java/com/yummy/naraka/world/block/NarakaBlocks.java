package com.yummy.naraka.world.block;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.references.BlockItemId;
import com.yummy.naraka.references.NarakaBlockIds;
import com.yummy.naraka.references.NarakaBlockItemIds;
import com.yummy.naraka.world.item.NarakaItemTooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
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
import java.util.function.Supplier;

public class NarakaBlocks {
    public static final HolderProxy<Block, TransparentBlock> TRANSPARENT_BLOCK = registerBlockWithItem(
            NarakaBlockItemIds.TRANSPARENT_BLOCK,
            TransparentBlock::new,
            from(Blocks.BEDROCK)
                    .forceSolidOn()
                    .noTerrainParticles()
                    .noLootTable()
    );

    public static final HolderProxy<Block, DiamondGolemSpawner> DIAMOND_GOLEM_SPAWNER = registerBlock(
            NarakaBlockIds.DIAMOND_GOLEM_SPAWNER,
            DiamondGolemSpawner::new,
            from(Blocks.SPAWNER)
                    .noLootTable()
                    .noTerrainParticles()
                    .noCollission()
    );

    public static final HolderProxy<Block, DropExperienceBlock> AMETHYST_ORE = registerBlockWithItem(
            NarakaBlockItemIds.AMETHYST_ORE,
            properties -> new DropExperienceBlock(UniformInt.of(0, 2), properties),
            from(Blocks.STONE).requiresCorrectToolForDrops()
    );
    public static final HolderProxy<Block, DropExperienceBlock> DEEPSLATE_AMETHYST_ORE = registerBlockWithItem(
            NarakaBlockItemIds.DEEPSLATE_AMETHYST_ORE,
            properties -> new DropExperienceBlock(UniformInt.of(1, 2), properties),
            from(Blocks.DEEPSLATE).requiresCorrectToolForDrops()
    );

    public static final HolderProxy<Block, DropExperienceBlock> NECTARIUM_ORE = registerBlockWithItem(
            NarakaBlockItemIds.NECTARIUM_ORE,
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
            Blocks.IRON_ORE
    );
    public static final HolderProxy<Block, DropExperienceBlock> DEEPSLATE_NECTARIUM_ORE = registerBlockWithItem(
            NarakaBlockItemIds.DEEPSLATE_NECTARIUM_ORE,
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties),
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
            item().rarity(Rarity.RARE)
                    .component(DataComponents.LORE, NarakaItemTooltip.NECTARIUM_CORE.itemLore())
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
            item().rarity(Rarity.EPIC)
                    .component(DataComponents.LORE, NarakaItemTooltip.SOUL_SMITHING_BLOCK.itemLore())
    );

    public static final HolderProxy<Block, Block> IMITATION_GOLD_BLOCK = registerBlockWithItem(
            NarakaBlockItemIds.IMITATION_GOLD_BLOCK,
            properties -> new EncroachingBlock(properties.strength(5, 6), Blocks.IRON_BLOCK),
            Blocks.GOLD_BLOCK,
            item().rarity(Rarity.UNCOMMON)
                    .component(DataComponents.LORE, NarakaItemTooltip.IMITATION_GOLD.itemLore())
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
            item().rarity(Rarity.EPIC)
                    .component(DataComponents.LORE, NarakaItemTooltip.HEROBRINE_TOTEM.itemLore())
    );

    public static final HolderProxy<Block, SoulStabilizer> SOUL_STABILIZER = registerBlockWithItem(
            NarakaBlockItemIds.SOUL_STABILIZER,
            SoulStabilizer::new,
            Blocks.GLASS,
            item().component(DataComponents.LORE, NarakaItemTooltip.SOUL_STABILIZER.itemLore())
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

    public static final List<HolderProxy<Block, Block>> SOUL_INFUSED_BLOCKS = List.of(
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
        for (Holder<Block> soulInfusedBlock : SOUL_INFUSED_BLOCKS)
            consumer.accept(soulInfusedBlock.value());
    }

    private static BlockBehaviour.Properties from(Block block) {
        return BlockBehaviour.Properties.ofFullCopy(block);
    }

    private static Item.Properties item() {
        return new Item.Properties();
    }

    private static HolderProxy<Block, Block> registerSoulInfusedBlock(BlockItemId id, Block baseBlock) {
        return registerSimpleBlockWithItem(id, baseBlock, item().fireResistant().rarity(Rarity.RARE));
    }

    private static HolderProxy<Block, Block> registerSoulInfusedBlock(BlockItemId id, Supplier<Block> baseBlock) {
        return registerBlockWithItem(id, Block::new, baseBlock, item().fireResistant().rarity(Rarity.RARE));
    }

    public static ResourceKey<Block> key(String name) {
        return ResourceKey.create(Registries.BLOCK, NarakaMod.location(name));
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlock(ResourceKey<Block> key, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(propertyBase);
        return RegistryProxy.register(Registries.BLOCK, key, () -> function.apply(properties));
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlock(ResourceKey<Block> key, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties properties) {
        return RegistryProxy.register(Registries.BLOCK, key, () -> function.apply(properties));
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        HolderProxy<Block, B> block = RegistryProxy.register(Registries.BLOCK, id.block(), () -> function.apply(blockProperties));
        RegistryProxy.register(Registries.ITEM, id.item(), () -> new BlockItem(block.get(), itemProperties));
        return block;
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> function, Supplier<Block> blockSupplier, Item.Properties itemProperties) {
        HolderProxy<Block, B> block = RegistryProxy.register(Registries.BLOCK, id.block(),
                () -> function.apply(
                        from(blockSupplier.get())
                )
        );
        RegistryProxy.register(Registries.ITEM, id.item(), () -> new BlockItem(block.get(), itemProperties));
        return block;
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(id, function, properties, item());
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase, Item.Properties itemProperties) {
        return registerBlockWithItem(id, function, from(propertyBase), itemProperties);
    }

    private static <B extends Block> HolderProxy<Block, B> registerBlockWithItem(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> function, Block propertyBase) {
        return registerBlockWithItem(id, function, BlockBehaviour.Properties.ofFullCopy(propertyBase));
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

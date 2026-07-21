package com.yummy.naraka.fabric.data.loot;

import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class NarakaBlockLootProvider extends FabricBlockLootTableProvider {
    private final LootItemCondition.Builder NECTARIUM_CRYSTAL_TOOLS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.PICKAXES));

    public NarakaBlockLootProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        dropSelf(NarakaBlocks.PURIFIED_SOUL_LAMP.getConcreteValue());
        dropSelf(NarakaBlocks.PURIFIED_SOUL_LANTERN.getConcreteValue());
        dropOre(NarakaBlocks.AMETHYST_ORE.getConcreteValue(), Items.AMETHYST_SHARD);
        dropOre(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.getConcreteValue(), Items.AMETHYST_SHARD);
        dropSelf(NarakaBlocks.NECTARIUM_BLOCK.getConcreteValue());
        dropOre(NarakaBlocks.NECTARIUM_ORE.getConcreteValue(), NarakaItems.NECTARIUM.getConcreteValue());
        dropOre(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.getConcreteValue(), NarakaItems.NECTARIUM.getConcreteValue());
        dropSelf(NarakaBlocks.IMITATION_GOLD_BLOCK.getConcreteValue());
        dropSelf(NarakaBlocks.AMETHYST_SHARD_BLOCK.getConcreteValue());

        NarakaBlocks.forEachSoulInfusedBlock(this::dropSelf);
        dropSelf(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.getConcreteValue());

        dropSelf(NarakaBlocks.HEROBRINE_TOTEM.getConcreteValue());

        dropWhenSilkTouch(NarakaBlocks.NECTARIUM_CORE_BLOCK.getConcreteValue());
        add(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.getConcreteValue(), this::createNectariumCrystalDrops);

        dropWhenSilkTouch(NarakaBlocks.SOUL_STABILIZER.getConcreteValue());
        dropSelf(NarakaBlocks.SOUL_SMITHING_BLOCK.getConcreteValue());
    }

    protected void dropOre(Block oreBlock, Item item) {
        add(oreBlock, block -> createOreDrop(block, item));
    }

    protected LootTable.Builder createNectariumCrystalDrops(Block block) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(block)
                                .when(BlockLootSubProvider.HAS_SILK_TOUCH)
                                .otherwise(
                                        LootItem.lootTableItem(NarakaItems.NECTARIUM.getConcreteValue())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))
                                                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
                                                .when(NECTARIUM_CRYSTAL_TOOLS)
                                                .otherwise(
                                                        LootItem.lootTableItem(NarakaItems.NECTARIUM.getConcreteValue())
                                                                .apply(ApplyExplosionDecay.explosionDecay())
                                                )
                                )
                        )
                );
    }
}

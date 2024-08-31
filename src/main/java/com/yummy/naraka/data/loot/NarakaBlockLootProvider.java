package com.yummy.naraka.data.loot;

import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
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

import java.util.concurrent.CompletableFuture;

public class NarakaBlockLootProvider extends FabricBlockLootTableProvider {
    private static final LootItemCondition.Builder NECTARIUM_CRYSTAL_TOOLS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.PICKAXES));

    public NarakaBlockLootProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(NarakaBlocks.NECTARIUM_BLOCK);
        dropOre(NarakaBlocks.NECTARIUM_ORE, NarakaItems.NECTARIUM);
        dropOre(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, NarakaItems.NECTARIUM);
        dropSelf(NarakaBlocks.SOUL_CRAFTING_BLOCK);
        dropSelf(NarakaBlocks.FORGING_BLOCK);
        dropSelf(NarakaBlocks.COMPRESSED_IRON_BLOCK);
        dropSelf(NarakaBlocks.IMITATION_GOLD_BLOCK);
        dropSelf(NarakaBlocks.AMETHYST_SHARD_BLOCK);

        NarakaBlocks.forEachSoulInfusedBlock(this::dropSelf);
        dropSelf(NarakaBlocks.PURIFIED_SOUL_BLOCK);
        dropSelf(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK);

        dropSelf(NarakaBlocks.EBONY_LOG);
        dropSelf(NarakaBlocks.STRIPPED_EBONY_LOG);
        dropSelf(NarakaBlocks.EBONY_WOOD);
        dropSelf(NarakaBlocks.STRIPPED_EBONY_WOOD);
        dropSelf(NarakaBlocks.HARD_EBONY_PLANKS);

        add(NarakaBlocks.EBONY_LEAVES, createLeavesDrops(NarakaBlocks.EBONY_LEAVES, NarakaBlocks.EBONY_SAPLING, 0.01f));
        dropSelf(NarakaBlocks.EBONY_SAPLING);
        dropWhenSilkTouch(NarakaBlocks.HEROBRINE_TOTEM);
        dropPottedContents(NarakaBlocks.POTTED_EBONY_SAPLING);
        dropSelf(NarakaBlocks.EBONY_ROOTS);
        dropSelf(NarakaBlocks.EBONY_METAL_BLOCK);

        add(NarakaBlocks.NECTARIUM_CORE_BLOCK, this::createSilkTouchOnlyTable);
        add(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK, this::createNectariumCrystalDrops);
    }

    protected void dropOre(Block oreBlock, Item item) {
        add(oreBlock, block -> createOreDrop(block, item));
    }

    protected LootTable.Builder createNectariumCrystalDrops(Block block) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(block)
                                .when(hasSilkTouch())
                                .otherwise(
                                        LootItem.lootTableItem(NarakaItems.NECTARIUM)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))
                                                .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
                                                .when(NECTARIUM_CRYSTAL_TOOLS)
                                                .otherwise(
                                                        LootItem.lootTableItem(NarakaItems.NECTARIUM)
                                                                .apply(ApplyExplosionDecay.explosionDecay())
                                                )
                                )
                        )
                );
    }
}

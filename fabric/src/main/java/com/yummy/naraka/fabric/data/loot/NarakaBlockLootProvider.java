package com.yummy.naraka.fabric.data.loot;

import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
    private final HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
    private final LootItemCondition.Builder NECTARIUM_CRYSTAL_TOOLS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ItemTags.PICKAXES));

    public NarakaBlockLootProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(NarakaBlocks.PURIFIED_SOUL_LAMP.get());
        dropSelf(NarakaBlocks.PURIFIED_SOUL_LANTERN.get());
        dropOre(NarakaBlocks.AMETHYST_ORE.get(), Items.AMETHYST_SHARD);
        dropOre(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get(), Items.AMETHYST_SHARD);
        dropSelf(NarakaBlocks.NECTARIUM_BLOCK.get());
        dropOre(NarakaBlocks.NECTARIUM_ORE.get(), NarakaItems.NECTARIUM.get());
        dropOre(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get(), NarakaItems.NECTARIUM.get());
        dropSelf(NarakaBlocks.COMPRESSED_IRON_BLOCK.get());
        dropSelf(NarakaBlocks.IMITATION_GOLD_BLOCK.get());
        dropSelf(NarakaBlocks.AMETHYST_SHARD_BLOCK.get());

        NarakaBlocks.forEachSoulInfusedBlock(this::dropSelf);
        dropSelf(NarakaBlocks.PURIFIED_SOUL_BLOCK.get());
        dropSelf(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get());

        dropSelf(NarakaBlocks.HEROBRINE_TOTEM.get());

        dropWhenSilkTouch(NarakaBlocks.NECTARIUM_CORE_BLOCK.get());
        add(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get(), this::createNectariumCrystalDrops);

        dropWhenSilkTouch(NarakaBlocks.SOUL_STABILIZER.get());
        dropSelf(NarakaBlocks.SOUL_SMITHING_BLOCK.get());
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
                                        LootItem.lootTableItem(NarakaItems.NECTARIUM.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))
                                                .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
                                                .when(NECTARIUM_CRYSTAL_TOOLS)
                                                .otherwise(
                                                        LootItem.lootTableItem(NarakaItems.NECTARIUM.get())
                                                                .apply(ApplyExplosionDecay.explosionDecay())
                                                )
                                )
                        )
                );
    }
}

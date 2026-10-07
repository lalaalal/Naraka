package com.yummy.naraka.fabric.data.loot;

import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.concurrent.CompletableFuture;

public class NarakaEntityLootProvider extends FabricEntityLootSubProvider {
    public NarakaEntityLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        add(NarakaEntityTypes.HEROBRINE.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(NarakaItems.GOD_BLOOD.get()))
                                .when(AllOfCondition.allOf(
                                        LootItemKilledByPlayerCondition.killedByPlayer(),
                                        LootItemRandomChanceCondition.randomChance(0.1f)
                                ))
                        ).withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(NarakaItems.HEROBRINE_SCARF.get()))
                                .add(LootItem.lootTableItem(NarakaItems.NARAKA_PICKAXE.get()))
                                .when(AllOfCondition.allOf(
                                        LootItemKilledByPlayerCondition.killedByPlayer(),
                                        LootItemRandomChanceCondition.randomChance(0.05f)
                                ))
                        ).withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .setBonusRolls(ContextFloatProviders.exactly(0.3f))
                                .add(LootItem.lootTableItem(NarakaItems.HEROBRINE_PHASE_1_DISC.get()))
                                .add(LootItem.lootTableItem(NarakaItems.HEROBRINE_PHASE_2_DISC.get()))
                                .add(LootItem.lootTableItem(NarakaItems.HEROBRINE_PHASE_3_DISC.get()))
                                .add(LootItem.lootTableItem(NarakaItems.HEROBRINE_PHASE_4_DISC.get()))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(enchantments, 0.1f, 0.08f))
                        ).withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get())
                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(1)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, ContextFloatProviders.between(0, 1)))
                                )
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(enchantments, 0.3f, 0.08f))
                        ).withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get()))
                        ).withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(NarakaItems.STIGMA_ROD.get()))
                                .when(AllOfCondition.allOf(
                                        LootItemKilledByPlayerCondition.killedByPlayer(),
                                        LootItemRandomChanceCondition.randomChance(0.0001f)
                                ))
                        )
        );
        add(NarakaEntityTypes.DIAMOND_GOLEM.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(Items.DIAMOND_BLOCK))
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        )
                        .withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(Items.COBBLESTONE)
                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3)))
                                )
                        )
                        .withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(Items.DIAMOND)
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, ContextFloatProviders.between(1, 4)))
                                )
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        )
        );
    }
}

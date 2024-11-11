package com.yummy.naraka.fabric.data.loot;

import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class NarakaEntityLootProvider extends SimpleFabricLootTableProvider {
    @Nullable
    private HolderLookup.Provider registries;

    public NarakaEntityLootProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.ENTITY);

        registryLookup.thenApply(provider -> this.registries = provider);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> generator) {
        if (registries == null)
            throw new IllegalStateException("Lookup provider not allocated");
        generator.accept(
                NarakaEntityTypes.HEROBRINE.get().getDefaultLootTable(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(NarakaItems.GOD_BLOOD.get()))
                                .when(AllOfCondition.allOf(
                                        LootItemKilledByPlayerCondition.killedByPlayer(),
                                        LootItemRandomChanceCondition.randomChance(0.01f)
                                ))
                        ).withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .setBonusRolls(ConstantValue.exactly(0.3f))
                                .add(LootItem.lootTableItem(NarakaItems.HEROBRINE_PHASE_1_DISC.get()))
                                .add(LootItem.lootTableItem(NarakaItems.HEROBRINE_PHASE_2_DISC.get()))
                                .add(LootItem.lootTableItem(NarakaItems.HEROBRINE_PHASE_3_DISC.get()))
                                .add(LootItem.lootTableItem(NarakaItems.HEROBRINE_PHASE_4_DISC.get()))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.1f, 0.08f))
                        ).withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get()))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.3f, 0.08f))
                        ).withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get()))
                        ).withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(NarakaItems.STIGMA_ROD.get()))
                                .when(AllOfCondition.allOf(
                                        LootItemKilledByPlayerCondition.killedByPlayer(),
                                        LootItemRandomChanceCondition.randomChance(0.0001f)
                                ))
                        )
        );
    }
}

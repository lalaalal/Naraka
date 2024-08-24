package com.yummy.naraka.data.loot;

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
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class NarakaEntityLootProvider extends SimpleFabricLootTableProvider {
    public NarakaEntityLootProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.ENTITY);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> generator) {
        generator.accept(
                NarakaEntityTypes.HEROBRINE.getDefaultLootTable(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(0.012f))
                                .setBonusRolls(ConstantValue.exactly(0.008f))
                                .add(LootItem.lootTableItem(NarakaItems.GOD_BLOOD)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                )
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        ).withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        )
        );
    }
}

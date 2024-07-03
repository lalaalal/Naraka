package com.yummy.naraka.data.loot;

import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

public class NarakaEntityLootSubProvider extends EntityLootSubProvider {
    protected NarakaEntityLootSubProvider(HolderLookup.Provider provider) {
        super(FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    public void generate() {
        add(
                NarakaEntityTypes.HEROBRINE.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(0.006f))
                                .setBonusRolls(ConstantValue.exactly(0.004f))
                                .add(LootItem.lootTableItem(NarakaItems.GOD_BLOOD)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                )
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        )
        );
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return NarakaEntityTypes.getKnownEntityTypes();
    }
}

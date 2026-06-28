package com.yummy.naraka.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;

public final class LootEvents {
    public static final Event<ModifyLootTable> MODIFY_LOOT_TABLE = Event.create(listeners -> (key, context) -> {
        for (ModifyLootTable listener : listeners)
            listener.modify(key, context);
    });

    @FunctionalInterface
    public interface ModifyLootTable {
        void modify(ResourceLocation location, Context context);
    }

    @FunctionalInterface
    public interface Context {
        void addPool(LootPool.Builder pool);
    }
}

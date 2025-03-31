package com.yummy.naraka.fabric.init;

import com.yummy.naraka.event.*;
import com.yummy.naraka.proxy.MethodProxy;
import com.yummy.naraka.world.item.NarakaCreativeModeTabs;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings("unused")
public class FabricEventHandler {
    @MethodProxy(EventHandler.class)
    public static void prepare() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> ServerEvents.SERVER_STARTING.invoker().run(server));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> ServerEvents.SERVER_STARTED.invoker().run(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerEvents.SERVER_STOPPING.invoker().run(server));
        ServerWorldEvents.LOAD.register((server, level) -> ServerEvents.SERVER_LEVEL_LOAD.invoker().run(level));

        ServerTickEvents.START_SERVER_TICK.register(server -> ServerEvents.SERVER_TICK_PRE.invoker().run(server));
        ServerTickEvents.END_SERVER_TICK.register(server -> ServerEvents.SERVER_TICK_POST.invoker().run(server));

        LootTableEvents.MODIFY.register((key, builder, lootTableSource, provider) -> {
            LootEvents.MODIFY_LOOT_TABLE.invoker().modify(key, builder::withPool);
        });
    }

    @MethodProxy(EventHandler.class)
    public static Event<CreativeModeTabEvents.ModifyEntries> createModifyTabEntries(ResourceKey<CreativeModeTab> key) {
        return new ModifyTabEntriesEvent(key);
    }

    private static class ModifyTabEntriesEvent extends Event<CreativeModeTabEvents.ModifyEntries> {
        private ModifyTabEntriesEvent(ResourceKey<CreativeModeTab> key) {
            ItemGroupEvents.modifyEntriesEvent(key).register(entries -> {
                invoker().modify(new FabricTabEntries(entries));
            });
        }

        @Override
        public CreativeModeTabEvents.ModifyEntries invoker() {
            return entries -> {
                for (CreativeModeTabEvents.ModifyEntries listener : listeners)
                    listener.modify(entries);
            };
        }
    }

    private record FabricTabEntries(FabricItemGroupEntries entries) implements NarakaCreativeModeTabs.TabEntries {
        @Override
        public void addBefore(ItemLike pivot, ItemLike... items) {
            entries.addBefore(pivot, items);
        }

        @Override
        public void addAfter(ItemLike pivot, ItemLike... items) {
            entries.addAfter(pivot, items);
        }
    }
}

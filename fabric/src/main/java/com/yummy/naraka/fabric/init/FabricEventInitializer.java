package com.yummy.naraka.fabric.init;

import com.yummy.naraka.event.*;
import com.yummy.naraka.world.item.NarakaCreativeModeTabs;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;

public final class FabricEventInitializer implements EventInitializer, CreativeModeTabEvents.ModifyEntriesEventFactory {
    private final Map<ResourceKey<CreativeModeTab>, Event<CreativeModeTabEvents.EntryModifier>> cache = new HashMap<>();

    @Override
    public void initialize(PlatformEventAccess events) {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> ServerEvents.SERVER_STARTING.invoker().run(server));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> ServerEvents.SERVER_STARTED.invoker().run(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerEvents.SERVER_STOPPING.invoker().run(server));
        ServerWorldEvents.LOAD.register((server, level) -> ServerEvents.SERVER_LEVEL_LOAD.invoker().run(level));

        ServerTickEvents.START_SERVER_TICK.register(server -> ServerEvents.SERVER_TICK_PRE.invoker().run(server));
        ServerTickEvents.END_SERVER_TICK.register(server -> ServerEvents.SERVER_TICK_POST.invoker().run(server));

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            LootEvents.MODIFY_LOOT_TABLE.invoker().modify(id, tableBuilder::withPool);
        });
    }

    @Override
    public Event<CreativeModeTabEvents.EntryModifier> create(ResourceKey<CreativeModeTab> key) {
        return cache.computeIfAbsent(key, _key -> {
            CreativeModeTabEvents.ModifyTabEntriesEvent event = new CreativeModeTabEvents.ModifyTabEntriesEvent(key);
            registerFabricModifyEntriesEvent(event);
            return event;
        });
    }

    private static void registerFabricModifyEntriesEvent(CreativeModeTabEvents.ModifyTabEntriesEvent modifyTabEntriesEvent) {
        ItemGroupEvents.modifyEntriesEvent(modifyTabEntriesEvent.key).register(entries -> {
            modifyTabEntriesEvent.invoker().modify(new FabricTabEntries(entries));
        });
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

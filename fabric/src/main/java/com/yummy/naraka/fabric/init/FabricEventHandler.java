package com.yummy.naraka.fabric.init;

import com.yummy.naraka.event.CreativeModeTabEvents;
import com.yummy.naraka.event.EventHandler;
import com.yummy.naraka.event.LootEvents;
import com.yummy.naraka.event.ServerEvents;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.world.item.NarakaCreativeModeTabs;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.Util;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings("unused")
public final class FabricEventHandler {
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

    @MethodProxy(CreativeModeTabEvents.class)
    public static CreativeModeTabEvents.ModifyEntriesEventFactory getModifyEntriesEventFactory() {
        return key -> Util.make(new CreativeModeTabEvents.ModifyTabEntriesEvent(key), FabricEventHandler::registerFabricModifyEntriesEvent);
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

package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.event.*;
import com.yummy.naraka.world.item.NarakaCreativeModeTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;

public class NeoForgeEventHandler extends EventHandler {
    private final IEventBus commonBus;
    private final IEventBus modBus;

    public NeoForgeEventHandler(IEventBus modBus) {
        this.commonBus = NeoForge.EVENT_BUS;
        this.modBus = modBus;
    }

    @Override
    protected void prepare() {
        commonBus.addListener(ServerStartingEvent.class, event -> ServerEvents.SERVER_STARTING.invoker().run(event.getServer()));
        commonBus.addListener(ServerStartedEvent.class, event -> ServerEvents.SERVER_STARTED.invoker().run(event.getServer()));
        commonBus.addListener(ServerStoppingEvent.class, event -> ServerEvents.SERVER_STOPPING.invoker().run(event.getServer()));
        commonBus.addListener(LevelEvent.Load.class, event -> {
            if (event.getLevel() instanceof ServerLevel level)
                ServerEvents.SERVER_LEVEL_LOAD.invoker().run(level);
        });

        commonBus.addListener(ServerTickEvent.Pre.class, event -> ServerEvents.SERVER_TICK_PRE.invoker().run(event.getServer()));
        commonBus.addListener(ServerTickEvent.Post.class, event -> ServerEvents.SERVER_TICK_POST.invoker().run(event.getServer()));

        commonBus.addListener(LootTableLoadEvent.class, event -> {
            ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, event.getName());
            LootEvents.MODIFY_LOOT_TABLE.invoker().modify(key, pool -> {
                event.getTable().addPool(pool.build());
            });
        });
    }

    @Override
    public Event<CreativeModeTabEvents.ModifyEntries> createModifyTabEntries(ResourceKey<CreativeModeTab> key) {
        return new ModifyTabEntriesEvent(key);
    }

    public class ModifyTabEntriesEvent extends Event<CreativeModeTabEvents.ModifyEntries> {
        public ModifyTabEntriesEvent(ResourceKey<CreativeModeTab> key) {
            modBus.addListener(BuildCreativeModeTabContentsEvent.class, event -> {
                if (event.getTabKey().equals(key)) {
                    invoker().modify(new NeoForgeTabEntries(event));
                }
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

    private record NeoForgeTabEntries(BuildCreativeModeTabContentsEvent event)
            implements NarakaCreativeModeTabs.TabEntries {

        @Override
        public void addBefore(ItemLike pivot, ItemLike... items) {
            List.of(items).reversed()
                    .forEach(item -> event.insertBefore(new ItemStack(pivot), new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
        }

        @Override
        public void addAfter(ItemLike pivot, ItemLike... items) {
            List.of(items).reversed()
                    .forEach(item -> event.insertAfter(new ItemStack(pivot), new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
        }
    }
}

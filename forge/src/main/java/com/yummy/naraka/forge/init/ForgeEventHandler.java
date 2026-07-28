package com.yummy.naraka.forge.init;

import com.yummy.naraka.event.*;
import com.yummy.naraka.forge.NarakaEventBus;
import com.yummy.naraka.world.item.NarakaCreativeModeTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.*;

public final class ForgeEventHandler implements EventInitializer, CreativeModeTabEvents.ModifyEntriesEventFactory, NarakaEventBus {
    private final Map<ResourceKey<CreativeModeTab>, Event<CreativeModeTabEvents.EntryModifier>> cache = new HashMap<>();

    @Override
    public void initialize(EventInitializer.PlatformEventAccess events) {
        FORGE_BUS.addListener(EventPriority.NORMAL, false, ServerStartedEvent.class, event -> ServerEvents.SERVER_STARTING.invoker().run(event.getServer()));
        FORGE_BUS.addListener(EventPriority.NORMAL, false, ServerStartedEvent.class, event -> ServerEvents.SERVER_STARTED.invoker().run(event.getServer()));
        FORGE_BUS.addListener(EventPriority.NORMAL, false, ServerStoppingEvent.class, event -> ServerEvents.SERVER_STOPPING.invoker().run(event.getServer()));
        FORGE_BUS.addListener(EventPriority.NORMAL, false, LevelEvent.Load.class, event -> {
            if (event.getLevel() instanceof ServerLevel level)
                ServerEvents.SERVER_LEVEL_LOAD.invoker().run(level);
        });

        FORGE_BUS.addListener(EventPriority.NORMAL, false, TickEvent.ServerTickEvent.class, event -> {
            if (event.phase == TickEvent.Phase.START)
                ServerEvents.SERVER_TICK_PRE.invoker().run(event.getServer());
        });
        FORGE_BUS.addListener(EventPriority.NORMAL, false, TickEvent.ServerTickEvent.class, event -> {
            if (event.phase == TickEvent.Phase.END)
                ServerEvents.SERVER_TICK_POST.invoker().run(event.getServer());
        });

        FORGE_BUS.addListener(EventPriority.NORMAL, false, LootTableLoadEvent.class, event -> {
            LootEvents.MODIFY_LOOT_TABLE.invoker().modify(event.getName(), pool -> {
                event.getTable().addPool(pool.build());
            });
        });

        final EntityEvents.LivingHurt livingHurt = events.getNarakaInvoker(EntityEvents.LIVING_HURT);
        FORGE_BUS.addListener(EventPriority.NORMAL, false, LivingHurtEvent.class, event -> {
            float amount = livingHurt.modifyDamage(event.getEntity(), event.getSource(), event.getAmount());
            event.setAmount(amount);
        });
        final EntityEvents.LivingDamage livingDamage = events.getNarakaInvoker(EntityEvents.LIVING_DAMAGE);
        FORGE_BUS.addListener(EventPriority.NORMAL, false, LivingDamageEvent.class, event -> {
            float amount = livingDamage.modifyDamage(event.getEntity(), event.getSource(), event.getAmount());
            event.setAmount(amount);
        });

        events.setPlatformInvoker(EntityEvents.LIVING_HURT, ForgeHooks::onLivingHurt);
        events.setPlatformInvoker(EntityEvents.LIVING_DAMAGE, ForgeHooks::onLivingDamage);
    }

    @Override
    public Event<CreativeModeTabEvents.EntryModifier> create(ResourceKey<CreativeModeTab> key) {
        return cache.computeIfAbsent(key, _key -> {
            CreativeModeTabEvents.ModifyTabEntriesEvent event = new CreativeModeTabEvents.ModifyTabEntriesEvent(key);
            registerForgeBuildCreativeModeTabEvent(event);
            return event;
        });
    }

    private static void registerForgeBuildCreativeModeTabEvent(CreativeModeTabEvents.ModifyTabEntriesEvent modifyTabEntriesEvent) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, BuildCreativeModeTabContentsEvent.class, event -> {
            if (event.getTabKey().equals(modifyTabEntriesEvent.key)) {
                modifyTabEntriesEvent.invoker().modify(new ForgeTabEntries(event));
            }
        });
    }

    private record ForgeTabEntries(BuildCreativeModeTabContentsEvent event)
            implements NarakaCreativeModeTabs.TabEntries {

        @Override
        public void addBefore(ItemLike pivot, ItemLike... items) {
            List<ItemLike> itemList = new ArrayList<>(List.of(items));
            Collections.reverse(itemList);
            itemList.forEach(item -> event.getEntries().putBefore(new ItemStack(pivot), new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
        }

        @Override
        public void addAfter(ItemLike pivot, ItemLike... items) {
            List<ItemLike> itemList = new ArrayList<>(List.of(items));
            Collections.reverse(itemList);
            itemList.forEach(item -> event.getEntries().putAfter(new ItemStack(pivot), new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
        }
    }
}

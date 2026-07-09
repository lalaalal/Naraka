package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.event.*;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import com.yummy.naraka.world.item.NarakaCreativeModeTabs;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;

@SuppressWarnings("unused")
public final class NeoForgeEventHandler implements NarakaEventBus {
    @MethodProxy(EventHandler.class)
    public static void prepare(EventHandler.PlatformEventAccess events) {
        NEOFORGE_BUS.addListener(ServerStartingEvent.class, event -> ServerEvents.SERVER_STARTING.invoker().run(event.getServer()));
        NEOFORGE_BUS.addListener(ServerStartedEvent.class, event -> ServerEvents.SERVER_STARTED.invoker().run(event.getServer()));
        NEOFORGE_BUS.addListener(ServerStoppingEvent.class, event -> ServerEvents.SERVER_STOPPING.invoker().run(event.getServer()));
        NEOFORGE_BUS.addListener(LevelEvent.Load.class, event -> {
            if (event.getLevel() instanceof ServerLevel level)
                ServerEvents.SERVER_LEVEL_LOAD.invoker().run(level);
        });

        NEOFORGE_BUS.addListener(ServerTickEvent.Pre.class, event -> ServerEvents.SERVER_TICK_PRE.invoker().run(event.getServer()));
        NEOFORGE_BUS.addListener(ServerTickEvent.Post.class, event -> ServerEvents.SERVER_TICK_POST.invoker().run(event.getServer()));

        NEOFORGE_BUS.addListener(LootTableLoadEvent.class, event -> {
            ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, event.getName());
            LootEvents.MODIFY_LOOT_TABLE.invoker().modify(key, pool -> {
                event.getTable().addPool(pool.build());
            });
        });

        final EntityEvents.LivingHurt livingHurt = events.getNarakaInvoker(EntityEvents.LIVING_HURT);
        NEOFORGE_BUS.addListener(LivingIncomingDamageEvent.class, event -> {
            float amount = livingHurt.modifyDamage(event.getEntity(), event.getSource(), event.getAmount());
            event.setAmount(amount);
        });
        final EntityEvents.LivingDamage livingDamage = events.getNarakaInvoker(EntityEvents.LIVING_DAMAGE);
        NEOFORGE_BUS.addListener(LivingDamageEvent.Pre.class, event -> {
            float amount = livingDamage.modifyDamage(event.getEntity(), event.getSource(), event.getNewDamage());
            event.setNewDamage(amount);
        });

        events.setPlatformInvoker(EntityEvents.LIVING_HURT, (entity, source, amount) -> {
            DamageContainer damageContainer = new DamageContainer(source, amount);
            CommonHooks.onEntityIncomingDamage(entity, damageContainer);
            return damageContainer.getNewDamage();
        });
        events.setPlatformInvoker(EntityEvents.LIVING_DAMAGE, (entity, source, amount) -> {
            DamageContainer damageContainer = new DamageContainer(source, amount);
            CommonHooks.onLivingDamagePre(entity, damageContainer);
            return damageContainer.getNewDamage();
        });

    }

    @MethodProxy(CreativeModeTabEvents.class)
    public static CreativeModeTabEvents.ModifyEntriesEventFactory getModifyEntriesEventFactory() {
        return key -> Util.make(new CreativeModeTabEvents.ModifyTabEntriesEvent(key), NeoForgeEventHandler::registerNeoForgeBuildCreativeModeTabEvent);
    }

    private static void registerNeoForgeBuildCreativeModeTabEvent(CreativeModeTabEvents.ModifyTabEntriesEvent modifyTabEntriesEvent) {
        NARAKA_BUS.addListener(BuildCreativeModeTabContentsEvent.class, event -> {
            if (event.getTabKey().equals(modifyTabEntriesEvent.key)) {
                modifyTabEntriesEvent.invoker().modify(new NeoForgeTabEntries(event));
            }
        });
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

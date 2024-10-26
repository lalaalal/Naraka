package com.yummy.naraka.fabric;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public final class NarakaModFabric implements ModInitializer, NarakaInitializer {
    @Override
    public void onInitialize() {
        RegistryInitializer.allocateInstance(new FabricRegistryInitializer());

        NarakaMod.initialize(this);
    }

    @Override
    public RegistryFactory getRegistryFactory() {
        return new RegistryFactory() {
            @Override
            public <T> Registry<T> createSimple(ResourceKey<Registry<T>> key) {
                return FabricRegistryBuilder.createSimple(key)
                        .attribute(RegistryAttribute.SYNCED)
                        .buildAndRegister();
            }
        };
    }

    @Override
    public void modifyCreativeModeTab(ResourceKey<CreativeModeTab> tabKey, Consumer<NarakaCreativeModTabs.TabEntries> consumer) {
        ItemGroupEvents.modifyEntriesEvent(tabKey)
                .register(wrap(consumer));
    }

    private static ItemGroupEvents.ModifyEntries wrap(Consumer<NarakaCreativeModTabs.TabEntries> consumer) {
        return entries -> consumer.accept(new FabricTabEntries(entries));
    }

    private record FabricTabEntries(FabricItemGroupEntries entries) implements NarakaCreativeModTabs.TabEntries {
        public void addBefore(ItemLike pivot, ItemLike... items) {
            entries.addBefore(pivot, items);
        }

        @Override
        public void addAfter(ItemLike pivot, ItemLike... items) {
            entries.addAfter(pivot, items);
        }
    }
}

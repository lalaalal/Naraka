package com.yummy.naraka.fabric;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.fabric.init.FabricBiomeModifier;
import com.yummy.naraka.fabric.init.FabricRegistryFactory;
import com.yummy.naraka.fabric.init.FabricRegistryInitializer;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.init.RegistryInitializer;
import com.yummy.naraka.world.NarakaBiomes;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public final class NarakaModFabric implements ModInitializer, NarakaInitializer {
    @Override
    public void onInitialize() {
        RegistryInitializer.setInstance(new FabricRegistryInitializer());

        NarakaMod.initialize(this);
        NarakaMod.isModLoaded = true;
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runnable.run();
    }

    @Override
    public RegistryFactory getRegistryFactory() {
        return FabricRegistryFactory.INSTANCE;
    }

    @Override
    public void modifyCreativeModeTab(ResourceKey<CreativeModeTab> tabKey, Consumer<NarakaCreativeModTabs.TabEntries> consumer) {
        ItemGroupEvents.modifyEntriesEvent(tabKey)
                .register(wrap(consumer));
    }

    @Override
    public NarakaBiomes.Modifier getBiomeModifier() {
        return FabricBiomeModifier.INSTANCE;
    }

    private static ItemGroupEvents.ModifyEntries wrap(Consumer<NarakaCreativeModTabs.TabEntries> consumer) {
        return entries -> consumer.accept(new FabricTabEntries(entries));
    }

    private record FabricTabEntries(FabricItemGroupEntries entries) implements NarakaCreativeModTabs.TabEntries {
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

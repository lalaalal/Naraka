package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.Platform;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.init.RegistryInitializer;
import com.yummy.naraka.neoforge.init.NeoForgeBiomeModifier;
import com.yummy.naraka.neoforge.init.NeoForgeRegistryInitializer;
import com.yummy.naraka.world.NarakaBiomes;
import com.yummy.naraka.world.item.NarakaCreativeModeTabs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mod(NarakaMod.MOD_ID)
public final class NarakaModNeoForge implements NarakaInitializer {
    private final Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries = new HashMap<>();
    private final List<Runnable> runAfterRegistryLoaded = new ArrayList<>();
    private final NeoForgeRegistryFactory registryFactory = new NeoForgeRegistryFactory();
    private final NeoForgeCreativeModeTabModifier creativeModeTabModifier = new NeoForgeCreativeModeTabModifier();
    private final IEventBus bus;

    public NarakaModNeoForge(IEventBus bus) {
        this.bus = bus;
        NarakaMod.initialize(this);

        bus.addListener(this::commonSetup);
    }

    @Override
    public Platform getPlatform() {
        return NeoForgePlatform.INSTANCE;
    }

    @Override
    public RegistryInitializer getRegistryInitializer() {
        return NeoForgeRegistryInitializer.getInstance(bus, registries::get);
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runAfterRegistryLoaded.add(runnable);
    }

    @Override
    public NarakaBiomes.Modifier getBiomeModifier() {
        return NeoForgeBiomeModifier.INSTANCE;
    }

    @Override
    public RegistryFactory getRegistryFactory() {
        return registryFactory;
    }

    @Override
    public NarakaCreativeModeTabs.CreativeModeTabModifier getCreativeModeTabModifier() {
        return creativeModeTabModifier;
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        NarakaMod.isRegistryLoaded = true;
        for (Runnable runnable : runAfterRegistryLoaded)
            runnable.run();
    }

    private class NeoForgeCreativeModeTabModifier implements NarakaCreativeModeTabs.CreativeModeTabModifier {
        @Override
        public void modify(ResourceKey<CreativeModeTab> tabKey, Consumer<NarakaCreativeModeTabs.TabEntries> entries) {
            bus.addListener((Consumer<BuildCreativeModeTabContentsEvent>) event -> {
                if (tabKey.equals(event.getTabKey()))
                    entries.accept(new NeoForgeTabEntries(event));
            });
        }
    }

    private class NeoForgeRegistryFactory extends RegistryFactory {
        @Override
        public <T> Registry<T> createSimple(ResourceKey<Registry<T>> key) {
            Registry<T> registry = new RegistryBuilder<>(key)
                    .sync(true)
                    .maxId(128)
                    .defaultKey(NarakaMod.location("empty"))
                    .create();
            registries.put(key, registry);
            bus.addListener((Consumer<NewRegistryEvent>) event -> event.register(registry));
            return registry;
        }
    }

    private record NeoForgeTabEntries(BuildCreativeModeTabContentsEvent event)
            implements NarakaCreativeModeTabs.TabEntries {

        @Override
        public void addBefore(ItemLike pivot, ItemLike... items) {
            List.of(items).reversed()
                    .forEach(item -> event.insertBefore(
                                    new ItemStack(pivot),
                                    new ItemStack(item),
                                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                            )
                    );
        }

        @Override
        public void addAfter(ItemLike pivot, ItemLike... items) {
            List.of(items).reversed()
                    .forEach(item -> event.insertAfter(
                                    new ItemStack(pivot),
                                    new ItemStack(item),
                                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                            )
                    );
        }
    }
}

package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.init.RegistryInitializer;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
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
    private final Map<ResourceKey<CreativeModeTab>, Consumer<NarakaCreativeModTabs.TabEntries>> tabEntriesMap = new HashMap<>();
    private final List<Runnable> runAfterRegistryLoaded = new ArrayList<>();

    public NarakaModNeoForge(IEventBus bus, ModContainer container) {
        RegistryInitializer.allocateInstance(new NeoForgeRegistryInitializer(bus, registries::get));

        NarakaMod.initialize(this);

        bus.addListener(this::commonSetup);
        bus.addListener(this::createRegistries);
        bus.addListener(this::modifyCreativeModeTabs);
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runAfterRegistryLoaded.add(runnable);
    }

    @Override
    public void registerFeatureBiomeModifier(String name, TagKey<Biome> target, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> featureKeys) {

    }

    @Override
    public RegistryFactory getRegistryFactory() {
        return new NeoForgeRegistryFactory();
    }

    @Override
    public void modifyCreativeModeTab(ResourceKey<CreativeModeTab> tabKey, Consumer<NarakaCreativeModTabs.TabEntries> entries) {
        tabEntriesMap.put(tabKey, entries);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        NarakaMod.isModLoaded = true;
        for (Runnable runnable : runAfterRegistryLoaded)
            runnable.run();
    }

    public void createRegistries(NewRegistryEvent event) {
        for (Registry<?> registry : registries.values())
            event.register(registry);
    }

    public void modifyCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
        NeoForgeTabEntries tabEntries = new NeoForgeTabEntries(event);
        if (tabEntriesMap.containsKey(tabKey))
            tabEntriesMap.get(tabKey).accept(tabEntries);
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
            return registry;
        }
    }

    private record NeoForgeTabEntries(
            BuildCreativeModeTabContentsEvent event) implements NarakaCreativeModTabs.TabEntries {

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

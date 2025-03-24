package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.Platform;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.event.EventHandler;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.neoforge.init.NeoForgeBiomeModifier;
import com.yummy.naraka.neoforge.init.NeoForgeEventHandler;
import com.yummy.naraka.neoforge.init.NeoForgeRegistryInitializer;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(NarakaMod.MOD_ID)
public final class NarakaModNeoForge implements NarakaInitializer {
    private final Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries = new HashMap<>();
    private final List<Runnable> runAfterRegistryLoaded = new ArrayList<>();
    private final NeoForgeRegistryFactory registryFactory = new NeoForgeRegistryFactory();
    private final NeoForgeEventHandler neoForgeEventHandler;
    private final IEventBus bus;

    public NarakaModNeoForge(IEventBus bus) {
        this.bus = bus;
        this.neoForgeEventHandler = new NeoForgeEventHandler(bus);
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
    public EventHandler getEventHandler() {
        return neoForgeEventHandler;
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        NarakaMod.isRegistryLoaded = true;
        for (Runnable runnable : runAfterRegistryLoaded)
            runnable.run();
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
            bus.addListener(NewRegistryEvent.class, event -> event.register(registry));
            return registry;
        }
    }
}

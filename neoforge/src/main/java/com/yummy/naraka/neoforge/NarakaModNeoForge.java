package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.init.NarakaInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;

@Mod(NarakaMod.MOD_ID)
public final class NarakaModNeoForge implements NarakaInitializer {
    private final List<Registry<?>> registries = new ArrayList<>();

    public NarakaModNeoForge(IEventBus modBus, ModContainer container) {
        NarakaMod.prepareRegistries(this);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::createRegistries);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        NarakaMod.initialize(this);
    }

    public void createRegistries(NewRegistryEvent event) {
        for (Registry<?> registry : registries)
            event.register(registry);
    }

    @Override
    public RegistryFactory getRegistryFactory() {
        return new RegistryFactory() {
            @Override
            public <T> Registry<T> createSimple(ResourceKey<Registry<T>> key) {
                Registry<T> registry = new RegistryBuilder<>(key)
                        .sync(true)
                        .maxId(128)
                        .defaultKey(NarakaMod.location("empty"))
                        .create();
                registries.add(registry);
                return registry;
            }
        };
    }

    @Override
    public void registerCreativeModeTabs() {

    }
}

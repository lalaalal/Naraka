package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.NarakaInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
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

    public NarakaModNeoForge(IEventBus bus, ModContainer container) {
        NarakaMod.prepareRegistries(this);
        ProxyAppender.with(bus)
                .add(Registries.PARTICLE_TYPE)
                .add(Registries.SOUND_EVENT)
                .add(Registries.ENTITY_TYPE)
                .add(NarakaRegistries.Keys.ENTITY_DATA_TYPE)
                .add(Registries.MOB_EFFECT)
                .add(Registries.BLOCK)
                .add(NarakaRegistries.Keys.REINFORCEMENT_EFFECT)
                .add(Registries.ARMOR_MATERIAL)
                .add(Registries.DATA_COMPONENT_TYPE)
                .add(Registries.ITEM)
                .add(Registries.RECIPE_TYPE)
                .add(Registries.RECIPE_SERIALIZER)
                .add(NarakaRegistries.Keys.EQUIPMENT_SET)
                .add(Registries.BLOCK_ENTITY_TYPE)
                .add(Registries.MENU)
                .add(Registries.STRUCTURE_TYPE)
                .add(Registries.STRUCTURE_PIECE)
                .add(NarakaRegistries.Keys.STRUCTURE_PIECE_FACTORY)
                .add(Registries.STRUCTURE_PLACEMENT)
                .add(NarakaRegistries.Keys.HEIGHT_PROVIDER_TYPE)
                .add(NarakaRegistries.Keys.PROTECTION_PREDICATE)
                .add(Registries.ROOT_PLACER_TYPE);

        NarakaMod.initialize(this);

        bus.addListener(this::commonSetup);
        bus.addListener(this::createRegistries);
    }

    public void commonSetup(FMLCommonSetupEvent event) {

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

    private record ProxyAppender(IEventBus bus) {
        private static <T> RegistryProxy<T> proxy(ResourceKey<Registry<T>> registryKey, IEventBus bus) {
            return new NeoForgeRegistryProxy<>(registryKey, bus);
        }

        public static ProxyAppender with(IEventBus bus) {
            return new ProxyAppender(bus);
        }

        public <T> ProxyAppender add(ResourceKey<Registry<T>> registryKey) {
            RegistryInitializer.INSTANCE.add(proxy(registryKey, bus));
            return this;
        }
    }
}

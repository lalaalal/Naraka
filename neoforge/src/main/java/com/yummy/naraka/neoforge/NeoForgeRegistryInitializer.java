package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeRegistryInitializer extends RegistryInitializer {
    private final IEventBus bus;

    public NeoForgeRegistryInitializer(IEventBus bus) {
        this.bus = bus;
    }

    @Override
    protected <T> RegistryProxy<T> create(ResourceKey<Registry<T>> key) {
        return new NeoForgeRegistryProxy<>(key);
    }

    protected class NeoForgeRegistryProxy<T> implements RegistryProxy<T> {
        private final DeferredRegister<T> registry;

        public NeoForgeRegistryProxy(ResourceKey<Registry<T>> registryKey) {
            registry = DeferredRegister.create(registryKey, NarakaMod.MOD_ID);
        }

        @SuppressWarnings("unchecked")
        @Override
        public ResourceKey<Registry<T>> getRegistryKey() {
            return (ResourceKey<Registry<T>>) registry.getRegistryKey();
        }

        @Override
        public <V extends T> LazyHolder<T, V> register(String name, Supplier<V> value) {
            registry.register(name, value);
            return createHolder(name);
        }

        @Override
        public void onRegistrationFinished() {
            registry.register(bus);
        }
    }
}

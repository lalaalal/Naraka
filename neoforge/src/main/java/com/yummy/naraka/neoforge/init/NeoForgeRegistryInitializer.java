package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.RegistryInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public final class NeoForgeRegistryInitializer extends RegistryInitializer {
    @Nullable
    private static NeoForgeRegistryInitializer INSTANCE;

    private final IEventBus bus;
    private final Function<ResourceKey<? extends Registry<?>>, Registry<?>> narakaRegistryProvider;

    public static NeoForgeRegistryInitializer getInstance(IEventBus bus, Function<ResourceKey<? extends Registry<?>>, Registry<?>> narakaRegistryProvider) {
        if (INSTANCE == null)
            INSTANCE = new NeoForgeRegistryInitializer(bus, narakaRegistryProvider);
        return INSTANCE;
    }

    public NeoForgeRegistryInitializer(IEventBus bus, Function<ResourceKey<? extends Registry<?>>, Registry<?>> narakaRegistryProvider) {
        this.bus = bus;
        this.narakaRegistryProvider = narakaRegistryProvider;
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
        public Registry<T> getRegistry() {
            Registry<T> result = RegistryProxy.super.getRegistry();
            if (result == null)
                return (Registry<T>) narakaRegistryProvider.apply(registry.getRegistryKey());
            return result;
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

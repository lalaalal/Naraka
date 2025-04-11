package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import com.yummy.naraka.neoforge.NarakaEventBus;
import com.yummy.naraka.proxy.MethodProxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class NeoForgeRegistryProxyProvider extends RegistryProxyProvider implements NarakaEventBus {
    private static final NeoForgeRegistryProxyProvider INSTANCE = new NeoForgeRegistryProxyProvider();

    private final Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries = new HashMap<>();

    @SuppressWarnings("unused")
    @MethodProxy(RegistryProxyProvider.class)
    public static NeoForgeRegistryProxyProvider getInstance() {
        return INSTANCE;
    }

    public static <T> void addNarakaRegistry(ResourceKey<Registry<T>> key, Registry<T> registry) {
        INSTANCE.registries.put(key, registry);
    }

    private NeoForgeRegistryProxyProvider() {

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
                return (Registry<T>) registries.get(getRegistryKey());
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        public ResourceKey<Registry<T>> getRegistryKey() {
            return (ResourceKey<Registry<T>>) registry.getRegistryKey();
        }

        @Override
        public <V extends T> HolderProxy<T, V> register(String name, Supplier<V> value) {
            registry.register(name, value);
            return createHolder(name);
        }

        @Override
        public void onRegistrationFinished() {
            registry.register(NARAKA_BUS);
        }
    }
}

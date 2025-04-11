package com.yummy.naraka.fabric.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import com.yummy.naraka.proxy.MethodProxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public final class FabricRegistryProxyProvider extends RegistryProxyProvider {
    private static final FabricRegistryProxyProvider INSTANCE = new FabricRegistryProxyProvider();

    @SuppressWarnings("unused")
    @MethodProxy(RegistryProxyProvider.class)
    public static RegistryProxyProvider getInstance() {
        return INSTANCE;
    }

    private FabricRegistryProxyProvider() {

    }

    @Override
    protected <T> RegistryProxy<T> create(ResourceKey<Registry<T>> key) {
        return new FabricRegistryProxy<>(key);
    }

    protected static class FabricRegistryProxy<T> implements RegistryProxy<T> {
        private final ResourceKey<Registry<T>> key;

        public FabricRegistryProxy(ResourceKey<Registry<T>> key) {
            this.key = key;
        }

        @Override
        public ResourceKey<Registry<T>> getRegistryKey() {
            return key;
        }

        @Override
        public <V extends T> HolderProxy<T, V> register(String name, Supplier<V> value) {
            Registry.register(getRegistryOrThrow(), NarakaMod.location(name), value.get());
            return createHolder(name);
        }
    }
}

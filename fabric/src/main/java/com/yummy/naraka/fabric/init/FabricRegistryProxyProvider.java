package com.yummy.naraka.fabric.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import com.yummy.naraka.core.registries.RegistryWriter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class FabricRegistryProxyProvider extends RegistryProxyProvider {
    @Override
    protected <T> RegistryWriter<T> create(ResourceKey<Registry<T>> key) {
        return new FabricRegistryWriter<>(key);
    }

    private record FabricRegistryWriter<T>(ResourceKey<Registry<T>> key) implements RegistryWriter<T> {

        @Override
        public ResourceKey<? extends Registry<T>> getRegistryKey() {
            return key;
        }

        @Override
        public <V extends T> HolderProxy<T, V> register(String name, Supplier<V> value) {
            return register(NarakaMod.location(name), value);
        }

        @Override
        public <V extends T> HolderProxy<T, V> register(ResourceLocation id, Supplier<V> value) {
            Registry.register(getRegistryOrThrow(), id, value.get());
            return createHolder(id, value);
        }
    }
}

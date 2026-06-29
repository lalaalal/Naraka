package com.yummy.naraka.fabric.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryWriter;
import com.yummy.naraka.core.registries.RegistryWriterProvider;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public final class FabricRegistryWriterProvider extends RegistryWriterProvider {
    private static final FabricRegistryWriterProvider INSTANCE = new FabricRegistryWriterProvider();

    @SuppressWarnings("unused")
    @MethodProxy(RegistryWriterProvider.class)
    public static RegistryWriterProvider getInstance() {
        return INSTANCE;
    }

    private FabricRegistryWriterProvider() {

    }

    @Override
    protected <T> RegistryWriter<T> create(ResourceKey<Registry<T>> key) {
        return new FabricRegistryWriter<>(key);
    }

    protected static class FabricRegistryWriter<T> implements RegistryWriter<T> {
        private final ResourceKey<Registry<T>> key;

        public FabricRegistryWriter(ResourceKey<Registry<T>> key) {
            this.key = key;
        }

        @Override
        public ResourceKey<? extends Registry<T>> getRegistryKey() {
            return key;
        }

        @Override
        public <V extends T> HolderProxy<T, V> register(String name, Supplier<V> value) {
            Registry.register(getRegistryOrThrow(), NarakaMod.location(name), value.get());
            return createHolder(name, value);
        }
    }
}

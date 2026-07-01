package com.yummy.naraka.forge.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import com.yummy.naraka.core.registries.RegistryReader;
import com.yummy.naraka.core.registries.RegistryWriter;
import com.yummy.naraka.forge.NarakaEventBus;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ForgeRegistryProxyProvider extends RegistryProxyProvider implements NarakaEventBus {
    private static final ForgeRegistryProxyProvider INSTANCE = new ForgeRegistryProxyProvider();

    private final Map<ResourceKey<? extends Registry<?>>, RegistryReader<?>> registryReaderMap = new HashMap<>();

    public static <T> void addRegistryReader(ResourceKey<Registry<T>> registryKey, RegistryReader<T> registryReader) {
        INSTANCE.registryReaderMap.put(registryKey, registryReader);
    }

    @SuppressWarnings("unused")
    @MethodProxy(RegistryProxyProvider.class)
    public static ForgeRegistryProxyProvider getInstance() {
        return INSTANCE;
    }

    private ForgeRegistryProxyProvider() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<RegistryReader<T>> getRegistryReader(ResourceKey<Registry<T>> key) {
        if (registryReaderMap.containsKey(key))
            return Optional.of((RegistryReader<T>) registryReaderMap.get(key));
        return super.getRegistryReader(key);
    }

    @Override
    protected <T> RegistryWriter<T> create(ResourceKey<Registry<T>> key) {
        ForgeRegistryWriter<T> registryWriter = new ForgeRegistryWriter<>(key);
        registryWriter.register(NARAKA_BUS);
        return registryWriter;
    }

    protected static class ForgeRegistryWriter<T> implements RegistryWriter<T> {
        private final ResourceKey<Registry<T>> registryKey;
        private final Map<ResourceLocation, Supplier<? extends T>> entries = new HashMap<>();

        public ForgeRegistryWriter(ResourceKey<Registry<T>> registryKey) {
            this.registryKey = registryKey;
        }

        @Override
        public ResourceKey<? extends Registry<T>> getRegistryKey() {
            return registryKey;
        }

        @Override
        public <V extends T> HolderProxy<T, V> register(String name, Supplier<V> value) {
            entries.put(NarakaMod.location(name), value);
            return createHolder(name, value);
        }

        @Override
        public <V extends T> HolderProxy<T, V> createHolder(String name, Supplier<V> value) {
            ResourceKey<T> key = ResourceKey.create(registryKey, NarakaMod.location(name));
            return new HolderProxy<>(key);
        }

        @SuppressWarnings("unchecked")
        public void register(IEventBus eventBus) {
            eventBus.addListener((Consumer<RegisterEvent>) event -> {
                if (event.getRegistryKey().equals(registryKey)) {
                    entries.forEach((id, value) -> {
                        event.register(getRegistryKey(), id, (Supplier<T>) value);
                    });
                }
            });
        }
    }
}

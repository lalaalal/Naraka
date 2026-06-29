package com.yummy.naraka.forge.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryWriter;
import com.yummy.naraka.core.registries.RegistryWriterProvider;
import com.yummy.naraka.forge.NarakaEventBus;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ForgeRegistryWriterProvider extends RegistryWriterProvider implements NarakaEventBus {
    private static final ForgeRegistryWriterProvider INSTANCE = new ForgeRegistryWriterProvider();

    private final Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries = new HashMap<>();

    @SuppressWarnings("unused")
    @MethodProxy(RegistryWriterProvider.class)
    public static ForgeRegistryWriterProvider getInstance() {
        return INSTANCE;
    }

    public static <T> void addNarakaRegistry(ResourceKey<Registry<T>> key, IForgeRegistry<T> registry) {
        if (get(key) instanceof ForgeRegistryWriter<T> forgeRegistryWriter) {

        }
    }

    private ForgeRegistryWriterProvider() {

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
            return new HolderProxy<>(key, value);
        }

        @SuppressWarnings("unchecked")
        public void register(IEventBus eventBus) {
            eventBus.addListener((Consumer<RegisterEvent>) event -> {
                entries.forEach((id, value) -> {
                    event.register(getRegistryKey(), id, (Supplier<T>) value);
                });
            });
        }
    }
}

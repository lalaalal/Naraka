package com.yummy.naraka.forge.init;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class ForgeRegistryFactory extends RegistryFactory implements NarakaEventBus {
    private static final ForgeRegistryFactory INSTANCE = new ForgeRegistryFactory();
    public static final ResourceLocation WRAPPER_ID = ResourceLocation.fromNamespaceAndPath("forge", "registry_defaulted_wrapper");

    @SuppressWarnings("unused")
    @MethodProxy(RegistryFactory.class)
    public static RegistryFactory getInstance() {
        return INSTANCE;
    }

    private ForgeRegistryFactory() {

    }

    @Override
    public <T> RegistryProxy<T> createSimple(ResourceKey<Registry<T>> key) {
        ForgeRegistryProxy<T> delegateRegistry = new ForgeRegistryProxy<>();
        NARAKA_BUS.addListener(EventPriority.NORMAL, true, NewRegistryEvent.class, event -> {
            event.create(RegistryBuilder.<T>of(key.location())
                            .setMaxID(128)
                            .setDefaultKey(NarakaMod.location("empty")),
                    forgeRegistry -> {
                        ForgeRegistryWriterProvider.addNarakaRegistry(key, forgeRegistry);
                        delegateRegistry.setRegistry(forgeRegistry);
                    }
            );
        });
        return delegateRegistry;
    }

    private static class ForgeRegistryProxy<T> implements RegistryProxy<T> {
        @Nullable
        private IForgeRegistry<T> registry;

        public void setRegistry(IForgeRegistry<T> registry) {
            this.registry = registry;
        }

        @Override
        public Codec<T> codec() {
            if (registry == null)
                throw new IllegalStateException("Registry is null");
            return registry.getCodec();
        }

        @Override
        public Optional<? extends Holder<T>> getHolder(ResourceKey<T> key) {
            if (registry == null)
                return Optional.empty();
            return registry.getHolder(key);
        }

        @Override
        public Stream<T> values() {
            if (registry == null)
                return Stream.empty();
            return registry.getValues().stream();
        }
    }
}

package com.yummy.naraka.forge.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.core.registries.RegistryReader;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

public class ForgeRegistryFactory implements RegistryFactory, NarakaEventBus {
    public static final ResourceLocation WRAPPER_ID = ResourceLocation.fromNamespaceAndPath("forge", "registry_defaulted_wrapper");

    @Override
    public <T> RegistryReader<T> createSimple(ResourceKey<Registry<T>> key) {
        ForgeRegistryReader<T> delegateRegistry = new ForgeRegistryReader<>();
        NARAKA_BUS.addListener(EventPriority.NORMAL, true, NewRegistryEvent.class, event -> {
            event.create(RegistryBuilder.<T>of(key.location())
                            .hasTags()
                            .setMaxID(128)
                            .setDefaultKey(NarakaMod.location("empty"))
                            .onBake((iForgeRegistryInternal, registryManager) -> {
                                delegateRegistry.setRegistry(iForgeRegistryInternal);
                            }),
                    delegateRegistry::setRegistry
            );
            ForgeRegistryProxyProvider.addRegistryReader(key, delegateRegistry);
        });
        return delegateRegistry;
    }
}

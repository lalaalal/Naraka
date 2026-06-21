package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class NeoForgeRegistryFactory implements RegistryFactory, NarakaEventBus {
    @Override
    public <T> Registry<T> createSimple(ResourceKey<Registry<T>> key) {
        Registry<T> registry = new RegistryBuilder<>(key)
                .sync(true)
                .maxId(128)
                .defaultKey(NarakaMod.identifier("empty"))
                .create();
        NeoForgeRegistryProxyProvider.addNarakaRegistry(key, registry);
        NARAKA_BUS.addListener(NewRegistryEvent.class, event -> event.register(registry));
        return registry;
    }
}

package com.yummy.naraka.fabric;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.init.NarakaInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class NarakaModFabric implements ModInitializer, NarakaInitializer {
    @Override
    public void onInitialize() {
        NarakaMod.initialize(this);
    }

    @Override
    public RegistryFactory getRegistryFactory() {
        return new RegistryFactory() {
            @Override
            public <T> Registry<T> createSimple(ResourceKey<Registry<T>> key) {
                return FabricRegistryBuilder.createSimple(key)
                        .attribute(RegistryAttribute.SYNCED)
                        .buildAndRegister();
            }
        };
    }

    @Override
    public void registerCreativeModeTabs() {
        NarakaCreativeModTabs.initialize();
    }
}

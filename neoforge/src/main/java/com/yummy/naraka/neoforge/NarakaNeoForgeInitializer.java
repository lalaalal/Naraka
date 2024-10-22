package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.init.NarakaInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class NarakaNeoForgeInitializer implements NarakaInitializer {
    @Override
    public RegistryFactory getRegistryFactory() {
        return new RegistryFactory() {
            @Override
            public <T> Registry<T> createSimple(ResourceKey<Registry<T>> key) {
                return new RegistryBuilder<>(key)
                        .sync(true)
                        .maxId(128)
                        .defaultKey(NarakaMod.location("empty"))
                        .create();
            }
        };
    }

    @Override
    public void registerCreativeModeTabs() {

    }
}

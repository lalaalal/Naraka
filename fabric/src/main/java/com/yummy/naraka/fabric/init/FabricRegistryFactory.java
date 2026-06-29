package com.yummy.naraka.fabric.init;

import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.core.registries.RegistryReader;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class FabricRegistryFactory extends RegistryFactory {
    private static final FabricRegistryFactory INSTANCE = new FabricRegistryFactory();

    @SuppressWarnings("unused")
    @MethodProxy(RegistryFactory.class)
    public static FabricRegistryFactory getInstance() {
        return INSTANCE;
    }

    private FabricRegistryFactory() {

    }

    @Override
    public <T> RegistryReader<T> createSimple(ResourceKey<Registry<T>> key) {
        return new RegistryReader.Simple<>(FabricRegistryBuilder.createSimple(key)
                .attribute(RegistryAttribute.SYNCED)
                .buildAndRegister()
        );
    }
}

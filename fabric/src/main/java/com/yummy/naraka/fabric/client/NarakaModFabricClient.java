package com.yummy.naraka.fabric.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.init.NarakaClientInitializer;
import com.yummy.naraka.fabric.init.FabricSpecialModelRendererRegistry;
import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class NarakaModFabricClient implements ClientModInitializer, NarakaClientInitializer {
    @Override
    public void onInitializeClient() {
        MethodInvoker.register(FabricClientEventHandler.class);
        MethodInvoker.register(FabricModelLayerRegistry.class);
        MethodInvoker.register(FabricParticleProviderRegistry.class);
        MethodInvoker.register(FabricBlockEntityRendererRegistry.class);
        MethodInvoker.register(FabricEntityRendererRegistry.class);
        MethodInvoker.register(FabricScreenFactoryRegistry.class);
        MethodInvoker.register(FabricHudRendererRegistry.class);
        MethodInvoker.register(FabricKeyMappingRegistry.class);
        MethodInvoker.register(FabricRenderPipelineRegistry.class);
        MethodInvoker.register(FabricSpecialModelRendererRegistry.class);
        MethodInvoker.register(FabricDimensionSpecialEffectsRegistry.class);

        NarakaModClient.initialize(this);
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void registerClientReloadListener(String name, Supplier<PreparableReloadListener> listener) {
        ResourceLoader.get(PackType.CLIENT_RESOURCES)
                .registerReloader(NarakaMod.location("listener", name), listener.get());
    }
}

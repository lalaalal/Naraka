package com.yummy.naraka.fabric.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.init.NarakaClientInitializer;
import com.yummy.naraka.proxy.MethodInvoker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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
        MethodInvoker.register(FabricShaderRegistry.class);
        MethodInvoker.register(FabricScreenFactoryRegistry.class);
        MethodInvoker.register(FabricHunRendererRegistry.class);
        MethodInvoker.register(FabricItemPropertyRegistry.class);

        NarakaModClient.initialize(this);
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void registerResourceReloadListener(String name, Supplier<PreparableReloadListener> listener) {
        ResourceManagerHelper helper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        helper.registerReloadListener(new FabricResourceReloadListener(name, listener));
    }

    private record FabricResourceReloadListener(ResourceLocation name,
                                                PreparableReloadListener listener) implements IdentifiableResourceReloadListener {
        public FabricResourceReloadListener(String name, Supplier<PreparableReloadListener> listener) {
            this(NarakaMod.location("listener", name), listener.get());
        }

        @Override
        public ResourceLocation getFabricId() {
            return name;
        }

        @Override
        public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
            return listener.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
        }
    }
}

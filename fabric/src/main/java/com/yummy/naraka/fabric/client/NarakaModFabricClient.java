package com.yummy.naraka.fabric.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.particle.ParticleFactory;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import com.yummy.naraka.init.NarakaClientInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NarakaModFabricClient implements ClientModInitializer, NarakaClientInitializer {
    @Override
    public void onInitializeClient() {
        NarakaModClient.initialize(this);
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void registerCustomItemRenderer(Supplier<? extends ItemLike> item, Supplier<CustomRenderManager.CustomItemRenderer> renderer) {
        BuiltinItemRendererRegistry.INSTANCE.register(item.get(), renderer.get()::render);
    }

    @Override
    public void registerResourceReloadListener(String name, Supplier<PreparableReloadListener> listener) {
        ResourceManagerHelper helper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        helper.registerReloadListener(new FabricResourceReloadListener(name, listener));
    }

    @Override
    public void registerShader(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer) {
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id, format, consumer));
    }

    @Override
    public <T extends ParticleOptions> void registerParticle(Supplier<? extends ParticleType<T>> particle, ParticleFactory<T> provider) {
        ParticleFactoryRegistry.getInstance().register(particle.get(), provider::create);
    }

    @Override
    public <T extends ParticleOptions> void registerParticle(Supplier<? extends ParticleType<T>> particle, ParticleProvider<T> provider) {
        ParticleFactoryRegistry.getInstance().register(particle.get(), provider);
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

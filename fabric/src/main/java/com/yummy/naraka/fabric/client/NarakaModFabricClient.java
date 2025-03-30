package com.yummy.naraka.fabric.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.particle.ParticleFactory;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import com.yummy.naraka.init.NarakaClientInitializer;
import com.yummy.naraka.proxy.MethodInvoker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class NarakaModFabricClient implements ClientModInitializer, NarakaClientInitializer {
    @Override
    public void onInitializeClient() {
        MethodInvoker.register(FabricClientEventHandler.class);
        MethodInvoker.register(FabricModelLayerRegistry.class);

        NarakaModClient.initialize(this);
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void registerItemProperties(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function) {
        ItemProperties.register(item.asItem(), id, function);
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
    public <T extends BlockEntity> void registerBlockEntity(Supplier<? extends BlockEntityType<? extends T>> blockEntityType, BlockEntityRendererProvider<T> rendererProvider) {
        BlockEntityRenderers.register(blockEntityType.get(), rendererProvider);
    }

    @Override
    public <T extends Entity> void registerEntity(Supplier<? extends EntityType<? extends T>> entityType, EntityRendererProvider<T> rendererProvider) {
        EntityRendererRegistry.register(entityType.get(), rendererProvider);
    }

    @Override
    public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreenFactory(Supplier<? extends MenuType<M>> menuType, ScreenFactory<M, S> factory) {
        MenuScreens.register(menuType.get(), factory::create);
    }

    @Override
    public void registerHud(Supplier<? extends LayeredDraw.Layer> layerSupplier) {
        LayeredDraw.Layer layer = layerSupplier.get();
        HudRenderCallback.EVENT.register(layer::render);
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

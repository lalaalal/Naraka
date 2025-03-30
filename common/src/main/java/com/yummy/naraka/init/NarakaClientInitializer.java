package com.yummy.naraka.init;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.client.particle.ParticleFactory;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import com.yummy.naraka.core.registries.RegistryLoadedListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public interface NarakaClientInitializer extends RegistryLoadedListener {
    void registerItemProperties(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function);

    void registerCustomItemRenderer(Supplier<? extends ItemLike> item, Supplier<CustomRenderManager.CustomItemRenderer> renderer);

    void registerResourceReloadListener(String name, Supplier<PreparableReloadListener> listener);

    void registerShader(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer);

    <T extends ParticleOptions> void registerParticle(Supplier<? extends ParticleType<T>> particle, ParticleProvider<T> provider);

    <T extends ParticleOptions> void registerParticle(Supplier<? extends ParticleType<T>> particle, ParticleFactory<T> factory);

    default <T extends ParticleOptions> void registerParticle(Supplier<? extends ParticleType<T>> particle, ParticleProvider.Sprite<T> provider) {
        registerParticle(particle, ParticleFactory.from(provider));
    }

    <T extends BlockEntity> void registerBlockEntity(Supplier<? extends BlockEntityType<? extends T>> blockEntityType, BlockEntityRendererProvider<T> rendererProvider);

    <T extends Entity> void registerEntity(Supplier<? extends EntityType<? extends T>> entityType, EntityRendererProvider<T> rendererProvider);

    <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreenFactory(Supplier<? extends MenuType<M>> menuType, ScreenFactory<M, S> factory);

    void registerHud(Supplier<? extends LayeredDraw.Layer> layer);

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    interface ScreenFactory<M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> {
        S create(M menu, Inventory inventory, Component title);
    }
}

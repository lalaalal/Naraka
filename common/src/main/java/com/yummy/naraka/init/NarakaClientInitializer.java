package com.yummy.naraka.init;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.client.particle.ParticleFactory;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public interface NarakaClientInitializer extends RegistryLoadedListener {
    void registerCustomItemRenderer(Supplier<? extends ItemLike> item, Supplier<CustomRenderManager.CustomItemRenderer> renderer);

    void registerResourceReloadListener(String name, Supplier<PreparableReloadListener> listener);

    void registerShader(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer);

    <T extends ParticleOptions> void registerParticle(Supplier<? extends ParticleType<T>> particle, ParticleProvider<T> provider);

    <T extends ParticleOptions> void registerParticle(Supplier<? extends ParticleType<T>> particle, ParticleFactory<T> factory);

    default <T extends ParticleOptions> void registerParticle(Supplier<? extends ParticleType<T>> particle, ParticleProvider.Sprite<T> provider) {
        registerParticle(particle, ParticleFactory.from(provider));
    }
}

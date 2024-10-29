package com.yummy.naraka.init;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public interface NarakaClientInitializer {
    void registerCustomItemRenderer(Supplier<? extends ItemLike> item, Supplier<CustomRenderManager.CustomItemRenderer> renderer);

    void registerResourceReloadListener(String name, Supplier<PreparableReloadListener> listener);

    void registerShader(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer);
}

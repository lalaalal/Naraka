package com.yummy.naraka.init;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public interface NarakaClientInitializer {
    void registerCustomItemRenderer(ItemLike item, CustomRenderManager.CustomItemRenderer renderer);

    @Deprecated
    void registerBlockRenderLayer(RenderType renderType, Block... blocks);

    void registerShader(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer);
}

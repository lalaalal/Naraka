package com.yummy.naraka.fabric.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.renderer.CustomItemRenderManager;
import com.yummy.naraka.init.NarakaClientInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NarakaModFabricClient implements ClientModInitializer, NarakaClientInitializer {
    @Override
    public void onInitializeClient() {
        NarakaModClient.prepareInitialization();
        NarakaModClient.initializeClient(this);
    }

    @Override
    public void registerCustomItemRenderer(Supplier<? extends Block> block, CustomItemRenderManager.CustomItemRenderer renderer) {
        BuiltinItemRendererRegistry.INSTANCE.register(block.get(), renderer::render);
    }

    @Override
    public void registerBlockRenderLayer(RenderType renderType, Block... blocks) {
        BlockRenderLayerMap.INSTANCE.putBlocks(renderType, blocks);
    }

    @Override
    public void registerShader(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer) {
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id, format, consumer));
    }
}

package com.yummy.naraka.fabric.client;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.yummy.naraka.client.init.RenderPipelineRegistry;
import net.minecraft.client.renderer.RenderPipelines;

public final class FabricRenderPipelineRegistry implements RenderPipelineRegistry.Registrar {
    @Override
    public void register(RenderPipeline renderPipeline) {
        RenderPipelines.register(renderPipeline);
    }
}

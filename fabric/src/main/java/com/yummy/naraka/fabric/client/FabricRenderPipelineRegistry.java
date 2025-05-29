package com.yummy.naraka.fabric.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.yummy.naraka.client.init.RenderPipelineRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderPipelines;

@Environment(EnvType.CLIENT)
public final class FabricRenderPipelineRegistry {
    @MethodProxy(RenderPipelineRegistry.class)
    public static RenderPipeline register(RenderPipeline renderPipeline) {
        return RenderPipelines.register(renderPipeline);
    }
}

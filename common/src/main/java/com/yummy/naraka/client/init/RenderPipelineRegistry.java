package com.yummy.naraka.client.init;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class RenderPipelineRegistry {
    public static RenderPipeline register(RenderPipeline renderPipeline) {
        MethodInvoker.of(RenderPipelineRegistry.class, "register")
                .invoke(renderPipeline);
        return renderPipeline;
    }
}

package com.yummy.naraka.neoforge.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.yummy.naraka.client.init.RenderPipelineRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

@OnlyIn(Dist.CLIENT)
public final class NeoForgeRenderPipelineRegistry implements NarakaEventBus {
    @MethodProxy(RenderPipelineRegistry.class)
    public static RenderPipeline register(RenderPipeline renderPipeline) {
        NARAKA_BUS.addListener(RegisterRenderPipelinesEvent.class, event -> {
            event.registerPipeline(renderPipeline);
        });
        return renderPipeline;
    }
}

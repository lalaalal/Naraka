package com.yummy.naraka.neoforge.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.yummy.naraka.client.init.RenderPipelineRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

public final class NeoForgeRenderPipelineRegistry implements RenderPipelineRegistry.Registrar, NarakaEventBus {
    @Override
    public void register(RenderPipeline renderPipeline) {
        NARAKA_BUS.addListener(RegisterRenderPipelinesEvent.class, event -> {
            event.registerPipeline(renderPipeline);
        });
    }
}

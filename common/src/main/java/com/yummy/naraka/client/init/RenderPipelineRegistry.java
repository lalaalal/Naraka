package com.yummy.naraka.client.init;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.yummy.naraka.client.NarakaClientServices;

public abstract class RenderPipelineRegistry {
    public static RenderPipeline register(RenderPipeline renderPipeline) {
        NarakaClientServices.RENDER_PIPELINE_REGISTRY.register(renderPipeline);
        return renderPipeline;
    }

    public interface Registrar {
        void register(RenderPipeline renderPipeline);
    }
}

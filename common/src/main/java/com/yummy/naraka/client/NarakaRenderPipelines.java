package com.yummy.naraka.client;

import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.RenderPipelineRegistry;
import net.minecraft.client.renderer.RenderPipelines;

public final class NarakaRenderPipelines {
    public static final RenderPipeline.Snippet LONGINUS_SNIPPET = RenderPipeline.builder(
                    RenderPipelines.MATRICES_PROJECTION_SNIPPET,
                    RenderPipelines.FOG_SNIPPET,
                    RenderPipelines.GLOBALS_SNIPPET
            )
            .withVertexShader(NarakaMod.identifier("core/longinus"))
            .withFragmentShader(NarakaMod.identifier("core/longinus"))
            .withSampler("Sampler0")
            .withSampler("Sampler1")
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withShaderDefine("LONGINUS_LAYERS", 16)
            .buildSnippet();

    public static final RenderPipeline LONGINUS_CUTOUT = RenderPipelineRegistry.register(
            RenderPipeline.builder(LONGINUS_SNIPPET)
                    .withLocation(NarakaMod.identifier("pipeline/longinus_cutout"))
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
                    .withSampler("Sampler2")
                    .withShaderDefine("CUTOUT")
                    .build()
    );

    public static final RenderPipeline LONGINUS = RenderPipelineRegistry.register(
            RenderPipeline.builder(LONGINUS_SNIPPET)
                    .withLocation(NarakaMod.identifier("pipeline/longinus"))
                    .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
                    .build()
    );

    public static void initialize() {

    }
}

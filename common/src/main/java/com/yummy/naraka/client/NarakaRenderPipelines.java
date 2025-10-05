package com.yummy.naraka.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.RenderPipelineRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderPipelines;

@Environment(EnvType.CLIENT)
public final class NarakaRenderPipelines {
    public static final RenderPipeline.Snippet LONGINUS_SNIPPET = RenderPipeline.builder(
                    RenderPipelines.MATRICES_PROJECTION_SNIPPET,
                    RenderPipelines.FOG_SNIPPET,
                    RenderPipelines.GLOBALS_SNIPPET
            )
            .withVertexShader(NarakaMod.location("longinus"))
            .withFragmentShader(NarakaMod.location("longinus"))
            .withSampler("Sampler0")
            .withSampler("Sampler1")
            .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
            .buildSnippet();

    public static final RenderPipeline LONGINUS = RenderPipelineRegistry.register(
            RenderPipeline.builder(LONGINUS_SNIPPET)
                    .withLocation(NarakaMod.location("pipeline/longinus"))
                    .withShaderDefine("LONGINUS_LAYERS", 16)
                    .build()
    );

    public static void initialize() {

    }
}

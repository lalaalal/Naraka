package com.yummy.naraka.client;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.RenderPipelineRegistry;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;

public final class NarakaRenderPipelines {
    public static final RenderPipeline.Snippet LONGINUS_SNIPPET = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withVertexShader(NarakaMod.identifier("core/longinus"))
            .withFragmentShader(NarakaMod.identifier("core/longinus"))
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER1)
            .withShaderDefine("LONGINUS_LAYERS", 16)
            .buildSnippet();

    public static final RenderPipeline LONGINUS_CUTOUT = RenderPipelineRegistry.register(
            RenderPipeline.builder(LONGINUS_SNIPPET)
                    .withLocation(NarakaMod.identifier("pipeline/longinus_cutout"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
                    .withPrimitiveTopology(PrimitiveTopology.QUADS)
                    .withBindGroupLayout(BindGroupLayouts.SAMPLER2)
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .withShaderDefine("CUTOUT")
                    .build()
    );

    public static final RenderPipeline LONGINUS = RenderPipelineRegistry.register(
            RenderPipeline.builder(LONGINUS_SNIPPET)
                    .withLocation(NarakaMod.identifier("pipeline/longinus"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION)
                    .withPrimitiveTopology(PrimitiveTopology.QUADS)
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .build()
    );

    public static void initialize() {

    }
}

package com.yummy.naraka.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.RenderPipelineRegistry;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;

public final class NarakaRenderPipelines {
    public static final RenderPipeline.Snippet LONGINUS_SNIPPET = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
            .withBindGroupLayout(BindGroupLayouts.PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
            .withBindGroupLayout(BindGroupLayouts.FOG)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withVertexShader(NarakaMod.identifier("core/longinus"))
            .withFragmentShader(NarakaMod.identifier("core/longinus"))
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER1)
            .withColorTargetState(ColorTargetState.DEFAULT)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withShaderDefine("LONGINUS_LAYERS", 16)
            .buildSnippet();

    public static final RenderPipeline LONGINUS_CUTOUT = RenderPipelineRegistry.register(
            RenderPipeline.builder(LONGINUS_SNIPPET)
                    .withLocation(NarakaMod.identifier("pipeline/longinus_cutout"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
                    .withBindGroupLayout(BindGroupLayouts.SAMPLER2)
                    .withShaderDefine("CUTOUT")
                    .build()
    );

    public static final RenderPipeline LONGINUS = RenderPipelineRegistry.register(
            RenderPipeline.builder(LONGINUS_SNIPPET)
                    .withLocation(NarakaMod.identifier("pipeline/longinus"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION)
                    .build()
    );

    public static void initialize() {

    }
}

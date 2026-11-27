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

    public static final RenderPipeline RYOIKI_MASK = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
            .withBindGroupLayout(BindGroupLayouts.PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
            .withVertexShader(NarakaMod.identifier("core/ryoiki_mask"))
            .withFragmentShader(NarakaMod.identifier("core/ryoiki_mask"))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withLocation(NarakaMod.identifier("pipeline/ryoiki_mask"))
            .withColorTargetState(ColorTargetState.DEFAULT)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .build();

    public static final RenderPipeline CLEAR_HAND_MASK = RenderPipeline.builder()
            .withVertexShader("core/screenquad")
            .withFragmentShader(NarakaMod.identifier("core/clear_hand_mask"))
            .withLocation(NarakaMod.identifier("pipeline/clear_hand_mask"))
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            .withBindGroupLayout(BindGroupLayouts.IN_SAMPLER)
            .withColorTargetState(ColorTargetState.DEFAULT)
            .build();

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

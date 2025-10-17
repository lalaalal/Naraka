package com.yummy.naraka.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class NarakaRenderTypes {
    private static final Function<ResourceLocation, RenderType> LONGINUS_CUTOUT = Util.memoize(
            resourceLocation -> RenderType.create(
                    "longinus",
                    DefaultVertexFormat.POSITION_TEX,
                    VertexFormat.Mode.QUADS,
                    1536,
                    false,
                    false,
                    RenderType.CompositeState.builder()
                            .setShaderState(new RenderStateShard.ShaderStateShard(NarakaShaders::longinusCutout))
                            .setTextureState(
                                    RenderStateShard.MultiTextureStateShard.builder()
                                            .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                                            .add(NarakaTextures.LONGINUS, false, false)
                                            .add(resourceLocation, false, false)
                                            .build()
                            )
                            .createCompositeState(false)
            )
    );

    private static final RenderType LONGINUS = RenderType.create(
            "longinus",
            DefaultVertexFormat.POSITION,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(NarakaShaders::longinus))
                    .setTextureState(
                            RenderStateShard.MultiTextureStateShard.builder()
                                    .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                                    .add(NarakaTextures.LONGINUS, false, false)
                                    .build()
                    )
                    .createCompositeState(false)
    );

    public static RenderType longinus() {
        return LONGINUS;
    }

    public static RenderType longinusCutout(ResourceLocation texture) {
        return LONGINUS_CUTOUT.apply(texture);
    }

    public static void initialize() {

    }
}

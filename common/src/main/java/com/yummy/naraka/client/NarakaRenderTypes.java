package com.yummy.naraka.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class NarakaRenderTypes {
    private static final Function<ResourceLocation, RenderType> LONGINUS_CUTOUT = Util.memoize(
            resourceLocation -> RenderType.create(
                    "longinus",
                    1536,
                    false,
                    false,
                    NarakaRenderPipelines.LONGINUS_CUTOUT,
                    RenderType.CompositeState.builder()
                            .setTextureState(
                                    RenderStateShard.MultiTextureStateShard.builder()
                                            .add(AbstractEndPortalRenderer.END_SKY_LOCATION, false)
                                            .add(NarakaTextures.LONGINUS, false)
                                            .add(resourceLocation, false)
                                            .build()
                            )
                            .createCompositeState(false)
            )
    );

    private static final RenderType LONGINUS = RenderType.create(
            "longinus",
            1536,
            false,
            false,
            NarakaRenderPipelines.LONGINUS,
            RenderType.CompositeState.builder()
                    .setTextureState(
                            RenderStateShard.MultiTextureStateShard.builder()
                                    .add(AbstractEndPortalRenderer.END_SKY_LOCATION, false)
                                    .add(NarakaTextures.LONGINUS, false)
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

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
            texture -> RenderType.create(
                    "longinus_cutout",
                    1536,
                    false,
                    false,
                    NarakaRenderPipelines.LONGINUS_CUTOUT,
                    RenderType.CompositeState.builder()
                            .setTextureState(
                                    RenderStateShard.MultiTextureStateShard.builder()
                                            .add(AbstractEndPortalRenderer.END_SKY_LOCATION, false)
                                            .add(NarakaTextures.LONGINUS, false)
                                            .add(texture, false)
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

    private static final RenderType SPACE = RenderType.create(
            "space",
            1536,
            false,
            false,
            NarakaRenderPipelines.SPACE,
            RenderType.CompositeState.builder()
                    .setTextureState(
                            RenderStateShard.MultiTextureStateShard.builder()
                                    .add(NarakaTextures.SPACE, false)
                                    .build()
                    )
                    .createCompositeState(false)
    );

    private static final Function<ResourceLocation, RenderType> SPACE_CUTOUT = Util.memoize(
            texture -> RenderType.create(
                    "space_cutout",
                    1536,
                    false,
                    false,
                    NarakaRenderPipelines.SPACE_CUTOUT,
                    RenderType.CompositeState.builder()
                            .setTextureState(
                                    RenderStateShard.MultiTextureStateShard.builder()
                                            .add(NarakaTextures.SPACE, false)
                                            .add(texture, false)
                                            .build()
                            )
                            .createCompositeState(false)
            ));

    public static RenderType longinus() {
        return LONGINUS;
    }

    public static RenderType longinusCutout(ResourceLocation texture) {
        return LONGINUS_CUTOUT.apply(texture);
    }

    public static RenderType space() {
        return SPACE;
    }

    public static RenderType spaceCutout(ResourceLocation texture) {
        return SPACE_CUTOUT.apply(texture);
    }

    public static void initialize() {

    }
}

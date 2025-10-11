package com.yummy.naraka.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;

@Environment(EnvType.CLIENT)
public final class NarakaRenderTypes {
    public static final RenderType LONGINUS = RenderType.create(
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

    public static void initialize() {

    }
}

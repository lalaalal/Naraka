package com.yummy.naraka.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;

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
                                    .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                                    .add(NarakaTextures.LONGINUS, false, false)
                                    .build()
                    )
                    .createCompositeState(false)
    );

    public static RenderType longinus() {
        return LONGINUS;
    }
}

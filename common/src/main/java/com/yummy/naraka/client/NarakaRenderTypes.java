package com.yummy.naraka.client;

import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public final class NarakaRenderTypes {
    private static final Function<Identifier, RenderType> LONGINUS_CUTOUT = Util.memoize(texture -> RenderType.create(
            "longinus_cutout",
            RenderSetup.builder(NarakaRenderPipelines.LONGINUS_CUTOUT)
                    .withTexture("Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION)
                    .withTexture("Sampler1", NarakaTextures.LONGINUS)
                    .withTexture("Sampler2", texture)
                    .createRenderSetup()
    ));

    private static final RenderType LONGINUS = RenderType.create(
            "longinus",
            RenderSetup.builder(NarakaRenderPipelines.LONGINUS)
                    .withTexture("Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION)
                    .withTexture("Sampler1", NarakaTextures.LONGINUS)
                    .createRenderSetup()
    );

    public static RenderType longinus() {
        return LONGINUS;
    }

    public static RenderType longinusCutout(Identifier texture) {
        return LONGINUS_CUTOUT.apply(texture);
    }

    public static RenderType emissive() {
        if (NarakaClientContext.SHADER_ENABLED.getValue() && NarakaConfig.CLIENT.enableEmissiveRenderType.getValue())
            return RenderTypes.entityTranslucentEmissive(NarakaTextures.AREA_EFFECT);
        return RenderTypes.lightning();
    }

    public static void initialize() {

    }
}

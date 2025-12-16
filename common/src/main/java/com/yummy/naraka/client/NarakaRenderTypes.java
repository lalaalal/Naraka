package com.yummy.naraka.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
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

    private static final RenderType SPACE = RenderType.create(
            "space",
            RenderSetup.builder(NarakaRenderPipelines.SPACE)
                    .withTexture("Sampler0", NarakaTextures.SPACE)
                    .createRenderSetup()
    );

    private static final Function<Identifier, RenderType> SPACE_CUTOUT = Util.memoize(texture -> RenderType.create(
            "space_cutout",
            RenderSetup.builder(NarakaRenderPipelines.SPACE_CUTOUT)
                    .withTexture("Sampler0", NarakaTextures.SPACE)
                    .createRenderSetup()
    ));

    public static RenderType longinus() {
        return LONGINUS;
    }

    public static RenderType longinusCutout(Identifier texture) {
        return LONGINUS_CUTOUT.apply(texture);
    }

    public static RenderType space() {
        return SPACE;
    }

    public static RenderType spaceCutout(Identifier texture) {
        return SPACE_CUTOUT.apply(texture);
    }

    public static void initialize() {

    }
}

package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.client.NarakaTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public record WavingScarfTexture(ResourceLocation texture,
                                 int textureX, int textureY,
                                 int width, int height,
                                 int textureWidth, int textureHeight) {
    public static final WavingScarfTexture PHASE_2_BACK = new WavingScarfTexture(
            NarakaTextures.HEROBRINE_SCARF, 9, 27, 7, 15, 64, 64
    );
    public static final WavingScarfTexture PHASE_2_FRONT = new WavingScarfTexture(
            NarakaTextures.HEROBRINE_SCARF, 9, 27, 7, 15, 64, 64
    );
    public static final WavingScarfTexture PHASE_3 = new WavingScarfTexture(
            NarakaTextures.HEROBRINE_FINAL_SCARF, 9, 27, 7, 15, 64, 64
    );

    public float u() {
        return textureX / (float) textureWidth;
    }

    public float v() {
        return textureY / (float) textureHeight;
    }

    public float widthInRatio() {
        return width / (float) textureWidth;
    }

    public float heightInRatio() {
        return height / (float) textureHeight;
    }
}

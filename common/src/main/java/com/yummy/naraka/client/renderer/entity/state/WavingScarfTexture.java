package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.client.NarakaTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public record WavingScarfTexture(ResourceLocation herobrineTexture, ResourceLocation shadowTexture,
                                 int scarfX, int scarfY,
                                 int scarfWidth, int scarfHeight,
                                 int textureWidth, int textureHeight) {
    public static final WavingScarfTexture PHASE_2_BACK = new WavingScarfTexture(
            NarakaTextures.HEROBRINE_SCARF, NarakaTextures.SHADOW_HEROBRINE_SCARF, 8, 27, 5, 20, 64, 64
    );
    public static final WavingScarfTexture PHASE_2_FRONT = new WavingScarfTexture(
            NarakaTextures.HEROBRINE_SCARF, NarakaTextures.SHADOW_HEROBRINE_SCARF, 19, 27, 5, 21, 64, 64
    );
    public static final WavingScarfTexture PHASE_3 = new WavingScarfTexture(
            NarakaTextures.FINAL_HEROBRINE_SCARF, NarakaTextures.FINAL_HEROBRINE_SCARF, 0, 0, 30, 40, 128, 128
    );

    public ResourceLocation texture(boolean isShadow) {
        if (isShadow)
            return shadowTexture;
        return herobrineTexture;
    }

    public float u() {
        return scarfX / (float) textureWidth;
    }

    public float v() {
        return scarfY / (float) textureHeight;
    }

    public float widthInRatio() {
        return scarfWidth / (float) textureWidth;
    }

    public float heightInRatio() {
        return scarfHeight / (float) textureHeight;
    }
}

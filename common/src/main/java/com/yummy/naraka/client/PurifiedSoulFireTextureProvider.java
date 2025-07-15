package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;

@Environment(EnvType.CLIENT)
public class PurifiedSoulFireTextureProvider {
    @SuppressWarnings("deprecation")
    private static final Material PURIFIED_SOUL_FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, NarakaMod.location("block", "purified_soul_fire_0"));
    @SuppressWarnings("deprecation")
    private static final Material PURIFIED_SOUL_FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, NarakaMod.location("block", "purified_soul_fire_1"));

    public static TextureAtlasSprite modifyFire0() {
        return PURIFIED_SOUL_FIRE_0.sprite();
    }

    public static TextureAtlasSprite modifyFire1() {
        return PURIFIED_SOUL_FIRE_1.sprite();
    }
}

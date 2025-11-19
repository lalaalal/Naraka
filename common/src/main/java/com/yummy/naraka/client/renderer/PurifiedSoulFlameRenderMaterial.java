package com.yummy.naraka.client.renderer;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.Material;

@Environment(EnvType.CLIENT)
public class PurifiedSoulFlameRenderMaterial {
    public static final Material PURIFIED_SOUL_FIRE_0 = new Material(NarakaTextures.LOCATION_BLOCKS, NarakaMod.location("block", "purified_soul_fire_0"));

    public static final Material PURIFIED_SOUL_FIRE_1 = new Material(NarakaTextures.LOCATION_BLOCKS, NarakaMod.location("block", "purified_soul_fire_1"));
}

package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class NarakaPostEffects {
    public static final ResourceLocation MONOCHROME = postEffect("monochrome");
    public static final ResourceLocation RYOIKI_GRAY = postEffect("ryoiki_gray");
    public static final ResourceLocation RYOIKI_GREEN = postEffect("ryoiki_green");

    public static ResourceLocation postEffect(String name) {
        return NarakaMod.location("shaders/post/" + name + ".json");
    }
}

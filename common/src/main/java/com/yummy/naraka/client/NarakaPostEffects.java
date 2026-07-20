package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.ResourceLocation;

public class NarakaPostEffects {
    public static final ResourceLocation MONOCHROME = postEffect("monochrome");
    public static final ResourceLocation RYOIKI_GRAY = postEffect("ryoiki_gray");
    public static final ResourceLocation RYOIKI_GREEN = postEffect("ryoiki_green");

    public static ResourceLocation postEffect(String name) {
        return NarakaMod.location("shaders/post/" + name + ".json");
    }
}

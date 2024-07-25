package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class NarakaSprites {
    public static final ResourceLocation SOUL_CRAFTING_PROGRESS = NarakaMod.location("container", "soul_crafting/crafting_progress");
    public static final ResourceLocation SOUL_CRAFTING_LIT_PROGRESS = NarakaMod.location("container", "soul_crafting/lit_progress");
    public static final ResourceLocation SOUL_CRAFTING_FUEL_CHARGE = NarakaMod.location("container", "soul_crafting/fuel_charge");

    public static final ResourceLocation DEATHCOUNT_BACKGROUND = NarakaMod.location("hud", "deathcount_background");
    public static final ResourceLocation DEATHCOUNT_HEART = NarakaMod.location("hud", "deathcount_heart");
}

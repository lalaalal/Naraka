package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class NarakaSprites {
    public static final ResourceLocation SOUL_CRAFTING_PROGRESS = NarakaMod.location("container", "soul_crafting/crafting_progress");
    public static final ResourceLocation SOUL_CRAFTING_LIT_PROGRESS = NarakaMod.location("container", "soul_crafting/lit_progress");
    public static final ResourceLocation SOUL_CRAFTING_FUEL_CHARGE = NarakaMod.location("container", "soul_crafting/fuel_charge");

    public static final ResourceLocation STIGMA_BACKGROUND = NarakaMod.location("hud/stigma_background");
    public static final ResourceLocation STIGMA = NarakaMod.location("hud/stigma");
    public static final ResourceLocation DEATH_COUNT_BACKGROUND = NarakaMod.location("hud/death_count_background");
    public static final ResourceLocation DEATH_COUNT_HEART = NarakaMod.location("hud/death_count_heart");
}

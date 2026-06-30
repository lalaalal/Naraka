package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class NarakaSprites {
    public static final ResourceLocation STIGMA_BACKGROUND = hud("stigma_background.png");
    public static final ResourceLocation STIGMA = hud("stigma.png");
    public static final ResourceLocation DEATH_COUNT_BACKGROUND = hud("death_count_background.png");
    public static final ResourceLocation DEATH_COUNT_HEART = hud("death_count_heart.png");
    public static final ResourceLocation STIGMA_CONSUME = hud("stigma_consume.png");

    public static final ResourceLocation PROGRESS_SLOT_SEPARATOR = hud("progress_slot_separator.png");

    public static ResourceLocation hud(String name) {
        return NarakaMod.location("textures/gui/sprites/hud", name);
    }
}

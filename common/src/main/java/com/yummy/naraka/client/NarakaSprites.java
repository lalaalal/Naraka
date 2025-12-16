package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public final class NarakaSprites {
    public static final Identifier STIGMA_BACKGROUND = NarakaMod.location("hud/stigma_background");
    public static final Identifier STIGMA = NarakaMod.location("hud/stigma");
    public static final Identifier DEATH_COUNT_BACKGROUND = NarakaMod.location("hud/death_count_background");
    public static final Identifier DEATH_COUNT_HEART = NarakaMod.location("hud/death_count_heart");
    public static final Identifier STIGMA_CONSUME = NarakaMod.location("hud/stigma_consume");

    public static final Identifier HEART_CONTAINER = Identifier.withDefaultNamespace("hud/heart/container");
}

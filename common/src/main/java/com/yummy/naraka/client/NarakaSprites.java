package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public final class NarakaSprites {
    public static final Identifier STIGMA_BACKGROUND = NarakaMod.identifier("hud/stigma_background");
    public static final Identifier STIGMA = NarakaMod.identifier("hud/stigma");
    public static final Identifier DEATH_COUNT_BACKGROUND = NarakaMod.identifier("hud/death_count_background");
    public static final Identifier DEATH_COUNT_HEART = NarakaMod.identifier("hud/death_count_heart");
    public static final Identifier STIGMA_CONSUME = NarakaMod.identifier("hud/stigma_consume");

    public static final Identifier HEART_CONTAINER = Identifier.withDefaultNamespace("hud/heart/container");
}

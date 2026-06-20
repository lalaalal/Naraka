package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

public final class NarakaSprites {
    public static final Identifier STIGMA_BACKGROUND = NarakaMod.identifier("hud/stigma_background");
    public static final Identifier STIGMA = NarakaMod.identifier("hud/stigma");
    public static final Identifier DEATH_COUNT_BACKGROUND = NarakaMod.identifier("hud/death_count_background");
    public static final Identifier DEATH_COUNT_HEART = NarakaMod.identifier("hud/death_count_heart");
    public static final Identifier STIGMA_CONSUME = NarakaMod.identifier("hud/stigma_consume");

    public static final Identifier PROGRESS_SLOT_SEPARATOR = NarakaMod.identifier("hud/progress_slot_separator");

    public static final SpriteId PURIFIED_SOUL_FIRE_0 = Sheets.BLOCKS_MAPPER.apply(NarakaMod.identifier("purified_soul_fire_0"));
    public static final SpriteId PURIFIED_SOUL_FIRE_1 = Sheets.BLOCKS_MAPPER.apply(NarakaMod.identifier("purified_soul_fire_1"));
}

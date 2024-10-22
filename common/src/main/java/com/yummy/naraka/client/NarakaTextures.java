package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.ResourceLocation;

public class NarakaTextures {
    public static final ResourceLocation HEROBRINE = NarakaMod.location("textures", "entity/herobrine.png");
    public static final ResourceLocation HEROBRINE_EYE = NarakaMod.location("textures", "entity/herobrine_eye.png");

    public static final ResourceLocation LONGINUS = NarakaMod.location("textures", "entity/longinus.png");

    public static final ResourceLocation SPEAR = NarakaMod.location("textures", "entity/spear.png");
    public static final ResourceLocation MIGHTY_HOLY_SPEAR = NarakaMod.location("textures", "entity/mighty_holy_spear.png");

    public static final ResourceLocation FORGING_BLOCK = NarakaMod.location("textures", "entity/forging_block.png");

    public static final ResourceLocation SOUL_CRAFTING = NarakaMod.location("textures", "gui/container/soul_crafting.png");

    public static final ResourceLocation NARAKA_ADVANCEMENT_ROOT_BACKGROUND = texture("gui/advancements/backgrounds/naraka.png");

    private static ResourceLocation texture(String path) {
        return NarakaMod.location("textures", path);
    }
}

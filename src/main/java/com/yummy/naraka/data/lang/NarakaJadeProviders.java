package com.yummy.naraka.data.lang;

import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.ResourceLocation;

public enum NarakaJadeProviders {
    SOUL_CRAFTING_BLOCK, STIGMA;

    public final ResourceLocation location;
    public final String translationKey;

    NarakaJadeProviders() {
        this.location = NarakaMod.location(name().toLowerCase());
        this.translationKey = "config.jade.plugin_naraka.%s".formatted(location.getPath());
    }
}

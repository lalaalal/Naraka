package com.yummy.naraka.data.lang;

import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.Identifier;

public enum NarakaJadeProviderComponents {
    SOUL_CRAFTING_BLOCK, SOUL_STABILIZER, SOUL_SMITHING_BLOCK, NECTARIUM_CORE, ENTITY_DATA;

    public final Identifier location;
    public final String translationKey;

    NarakaJadeProviderComponents() {
        this.location = NarakaMod.location(name().toLowerCase());
        this.translationKey = "config.jade.plugin_naraka.%s".formatted(location.getPath());
    }
}

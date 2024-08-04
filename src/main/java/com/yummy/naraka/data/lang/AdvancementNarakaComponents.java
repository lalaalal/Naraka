package com.yummy.naraka.data.lang;

import net.minecraft.network.chat.Component;

import java.util.Locale;

public enum AdvancementNarakaComponents implements AdvancementComponent {
    ROOT,
    SANCTUARY_COMPASS,
    HEROBRINE_SANCTUARY,
    SUMMON_HEROBRINE,
    KILL_HEROBRINE,
    TAKE_EBONY_ROOT,
    GOD_BLOOD,
    EBONY_METAL,
    SOUL_INFUSING;

    public static final String ADVANCEMENT_NAME = "naraka";

    private final String titleKey;
    private final String descriptionKey;
    private final Component title;
    private final Component description;

    AdvancementNarakaComponents() {
        this.titleKey = AdvancementComponent.title(ADVANCEMENT_NAME, name().toLowerCase(Locale.ROOT));
        this.descriptionKey = AdvancementComponent.description(ADVANCEMENT_NAME, name().toLowerCase(Locale.ROOT));
        this.title = Component.translatable(titleKey);
        this.description = Component.translatable(descriptionKey);
    }

    @Override
    public String titleKey() {
        return titleKey;
    }

    @Override
    public String descriptionKey() {
        return descriptionKey;
    }

    @Override
    public Component title() {
        return title;
    }

    @Override
    public Component description() {
        return description;
    }
}

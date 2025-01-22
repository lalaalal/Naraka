package com.yummy.naraka.data.lang;

import com.yummy.naraka.NarakaMod;

public enum AdvancementNarakaComponents implements AdvancementComponent {
    ROOT,
    SANCTUARY_COMPASS,
    FIND_HEROBRINE_SANCTUARY,
    SUMMON_HEROBRINE,
    KILL_HEROBRINE,
    PURIFIED_SOUL_METAL,
    PURIFIED_SOUL_SWORD,
    GOD_BLOOD,
    SOUL_INFUSED_MATERIALS,
    STABILIZER,
    FILL_SOUL_STABILIZER,
    CHALLENGERS_BLESSING,
    SOUL_SWORDS,
    ULTIMATE_SWORD;

    @Override
    public String rootName() {
        return NarakaMod.MOD_ID;
    }

    @Override
    public String advancementName() {
        return name().toLowerCase();
    }
}

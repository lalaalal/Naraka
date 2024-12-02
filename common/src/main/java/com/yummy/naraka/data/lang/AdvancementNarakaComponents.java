package com.yummy.naraka.data.lang;

import com.yummy.naraka.NarakaMod;

public enum AdvancementNarakaComponents implements AdvancementComponent {
    ROOT,
    WAY_TO_HIM,
    HEROBRINE_SANCTUARY,
    SUMMON_HEROBRINE,
    KILL_HEROBRINE,
    SOAP,
    PURE_VESSEL,
    GOD_BLOOD,
    SOUL_INFUSED_MATERIALS,
    STABILIZER,
    FULLY_CHARGED,
    CHALLENGERS_BLESSING,
    RAINBOW,
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

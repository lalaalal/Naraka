package com.yummy.naraka.fabric.data.lang;

import com.yummy.naraka.NarakaMod;

public enum AdvancementExtraComponents implements AdvancementComponent {
    BUY_NECTARIUM_CORE,
    ACTIVATE_NECTARIUM_CORE,
    EAT_NECTARIUM,
    SOUL_INFUSED_NECTARIUM,
    STIGMA_ROD;

    @Override
    public String rootName() {
        return NarakaMod.MOD_ID + ".extra";
    }

    @Override
    public String advancementName() {
        return name().toLowerCase();
    }
}

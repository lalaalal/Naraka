package com.yummy.naraka.client.color;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.ItemTintSourceRegistry;

public class NarakaItemTintSources {
    public static void initialize() {
        ItemTintSourceRegistry.register(NarakaMod.identifier("rainbow"), RainbowTintSource.MAP_CODEC);
        ItemTintSourceRegistry.register(NarakaMod.identifier("custom_model_tint"), CustomModelTintSource.MAP_CODEC);
    }
}

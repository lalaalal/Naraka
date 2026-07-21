package com.yummy.naraka.fabric.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.yummy.naraka.Platform;
import com.yummy.naraka.client.config.NarakaConfigScreen;

public class NarakaModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (Platform.getInstance().modExists("cloth-config"))
            return NarakaConfigScreen::create;
        return ModMenuApi.super.getModConfigScreenFactory();
    }
}

package com.yummy.naraka.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.lang.LanguageKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class NarakaKeyMappings {
    public static final KeyMapping TOGGLE_ORE_SEE_THROUGH = new KeyMapping(
            LanguageKey.KEY_TOGGLE_ORE_SEE_THROUGH,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            Categories.NARAKA
    );

    public static class Categories {
        public static final KeyMapping.Category NARAKA = KeyMapping.Category.register(NarakaMod.location("naraka"));
    }
}

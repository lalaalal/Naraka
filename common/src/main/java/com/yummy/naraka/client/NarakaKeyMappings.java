package com.yummy.naraka.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.yummy.naraka.data.lang.LanguageKey;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class NarakaKeyMappings {
    public static final KeyMapping TOGGLE_ORE_SEE_THROUGH = new KeyMapping(
            LanguageKey.KEY_TOGGLE_ORE_SEE_THROUGH,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            LanguageKey.KEY_CATEGORIES_NARAKA
    );
}

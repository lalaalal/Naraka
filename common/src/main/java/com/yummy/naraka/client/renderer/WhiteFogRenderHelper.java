package com.yummy.naraka.client.renderer;

import com.yummy.naraka.client.NarakaClientContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class WhiteFogRenderHelper {
    private static final int MAX_WHITE_SCREEN_TICK = 20;

    private static int prevWhiteFogTickCount = 0;
    private static int whiteFogTickCount = 0;

    public static void tick() {
        prevWhiteFogTickCount = whiteFogTickCount;
        if (NarakaClientContext.ENABLE_WHITE_SCREEN.getValue()) {
            if (whiteFogTickCount < MAX_WHITE_SCREEN_TICK) {
                whiteFogTickCount += 1;
            }
        } else if (whiteFogTickCount > 0) {
            whiteFogTickCount -= 1;
        }
    }

    public static float getProgress(float partialTick) {
        if (prevWhiteFogTickCount < whiteFogTickCount)
            return (prevWhiteFogTickCount + partialTick) / (float) MAX_WHITE_SCREEN_TICK;
        if (prevWhiteFogTickCount > whiteFogTickCount)
            return (whiteFogTickCount - partialTick) / (float) MAX_WHITE_SCREEN_TICK;
        return whiteFogTickCount / (float) MAX_WHITE_SCREEN_TICK;
    }
}

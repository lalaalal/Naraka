package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.shaders.FogShape;
import com.yummy.naraka.client.NarakaClientContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.util.Mth;
import org.joml.Vector4f;

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

    public static boolean shouldApplyWhiteFog() {
        return whiteFogTickCount > 0;
    }

    public static FogParameters getWhiteFogParameters(Vector4f fogColor, float renderDistance, float partialTick) {
        float prevDistance = 4 * renderDistance * (1 - prevWhiteFogTickCount / (float) MAX_WHITE_SCREEN_TICK);
        float currentDistance = 4 * renderDistance * (1 - whiteFogTickCount / (float) MAX_WHITE_SCREEN_TICK);
        float distance = Mth.lerp(partialTick, prevDistance, currentDistance);
        Vector4f color = getFogColor(fogColor, partialTick);
        return new FogParameters(0, distance, FogShape.SPHERE, color.x, color.y, color.z, color.w);
    }

    public static Vector4f getFogColor(Vector4f original, float partialTick) {
        float delta = getProgress(partialTick);
        float x = Mth.lerp(Math.min(delta, 1), original.x, 1);
        float y = Mth.lerp(Math.min(delta, 1), original.y, 1);
        float z = Mth.lerp(Math.min(delta, 1), original.z, 1);
        return new Vector4f(x, y, z, 1);
    }

    public static float getProgress(float partialTick) {
        if (prevWhiteFogTickCount < whiteFogTickCount)
            return (prevWhiteFogTickCount + partialTick) / (float) MAX_WHITE_SCREEN_TICK;
        if (prevWhiteFogTickCount > whiteFogTickCount)
            return (whiteFogTickCount - partialTick) / (float) MAX_WHITE_SCREEN_TICK;
        return whiteFogTickCount / (float) MAX_WHITE_SCREEN_TICK;
    }
}

package com.yummy.naraka.client.gui.hud;

import com.yummy.naraka.client.renderer.WhiteFogRenderHelper;
import com.yummy.naraka.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;

@Environment(EnvType.CLIENT)
public class WhiteHud implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
        float alpha = WhiteFogRenderHelper.getProgress(partialTick);
        if (alpha <= 0)
            return;
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        guiGraphics.fill(0, 0, width, height, Color.of(alpha, 1, 1, 1).pack());
    }
}

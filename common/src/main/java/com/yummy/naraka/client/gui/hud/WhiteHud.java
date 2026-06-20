package com.yummy.naraka.client.gui.hud;

import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.renderer.WhiteFogRenderHelper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.ARGB;

public class WhiteHud implements HudRenderer {
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        if (NarakaClientContext.SHADER_ENABLED.getValue()) {
            float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
            float alpha = WhiteFogRenderHelper.getProgress(partialTick);
            if (alpha <= 0)
                return;
            int width = guiGraphics.guiWidth();
            int height = guiGraphics.guiHeight();
            guiGraphics.fill(0, 0, width, height, ARGB.white(WhiteFogRenderHelper.getProgress(partialTick)));
        }
    }
}

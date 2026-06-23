package com.yummy.naraka.client.gui.hud;

import com.yummy.naraka.client.renderer.WhiteFogRenderHelper;
import com.yummy.naraka.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;

@Environment(EnvType.CLIENT)
public class WhiteHud implements HudRenderer {
    @Override
    public void render(GuiGraphics guiGraphics, float partialTicks) {
        float alpha = WhiteFogRenderHelper.getProgress(partialTicks);
        if (alpha <= 0)
            return;
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        guiGraphics.fill(0, 0, width, height, Color.of(alpha, 1, 1, 1).pack());
    }
}

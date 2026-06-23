package com.yummy.naraka.client.gui.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;

@Environment(EnvType.CLIENT)
public interface HudRenderer {
    void render(GuiGraphics graphics, float partialTicks);
}

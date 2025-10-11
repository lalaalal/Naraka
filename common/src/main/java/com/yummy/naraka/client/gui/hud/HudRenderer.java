package com.yummy.naraka.client.gui.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

public interface HudRenderer {
    void render(GuiGraphics graphics, DeltaTracker deltaTracker);
}

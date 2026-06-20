package com.yummy.naraka.client.gui.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface HudRenderer {
    void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker);
}

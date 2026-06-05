package com.yummy.naraka.client.gui.components;

import com.yummy.naraka.client.NarakaSprites;
import com.yummy.naraka.world.overlay.NarakaProgressOverlayExtensionTypes;
import com.yummy.naraka.world.overlay.ProgressOverlayExtensionType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class HealthSlotOverlayExtension implements ProgressOverlayExtension<Integer> {
    public static final int PROGRESS_BAR_WIDTH = 182;

    public static final int SEPARATOR_WIDTH = 1;
    public static final int SEPARATOR_HEIGHT = 3;

    private final int maxSeparatorCount;
    private int separatorCount;
    private final float slotWidth;

    public HealthSlotOverlayExtension(int maxSeparatorCount) {
        this.maxSeparatorCount = maxSeparatorCount;
        this.separatorCount = maxSeparatorCount;
        this.slotWidth = (PROGRESS_BAR_WIDTH - 2f) / (maxSeparatorCount + 1);
    }

    @Override
    public ProgressOverlayExtensionType<Integer> getType() {
        return NarakaProgressOverlayExtensionTypes.ORIGIN_HEROBRINE.get();
    }

    @Override
    public void render(int x, int y, GuiGraphics graphics) {
        int baseY = y + 1;
        for (int count = 1; count <= separatorCount; count++) {
            int baseX = Math.round(x + 1 + slotWidth * count);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, NarakaSprites.PROGRESS_SLOT_SEPARATOR, baseX, baseY, SEPARATOR_WIDTH, SEPARATOR_HEIGHT);
        }
    }

    @Override
    public void update(Integer data) {
        this.separatorCount = Mth.clamp(data, 0, this.maxSeparatorCount);
    }
}

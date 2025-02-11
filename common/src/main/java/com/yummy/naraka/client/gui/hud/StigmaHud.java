package com.yummy.naraka.client.gui.hud;

import com.yummy.naraka.client.NarakaSprites;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class StigmaHud implements ClientGuiEvent.RenderHud {
    public static final int BACKGROUND_WIDTH = 16;
    public static final int BACKGROUND_HEIGHT = 21;
    public static final int STIGMA_SIZE = 2;
    public static final int STIGMA_START_X = 3;
    public static final int STIGMA_START_Y = 18;
    public static final int STIGMA_OFFSET_BORDER = 2;

    @Override
    public void renderHud(GuiGraphics guiGraphics, DeltaTracker tickCounter) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        int stigma = StigmaHelper.get(player).value();

        int baseX = DeathCountHud.BASE_X + DeathCountHud.BACKGROUND_WIDTH + 8;
        int baseY = DeathCountHud.BASE_Y + (DeathCountHud.BACKGROUND_HEIGHT - BACKGROUND_HEIGHT) / 2;

        int deathCount = DeathCountHelper.get(player);

        if (deathCount <= 0 && stigma < 1)
            return;
        if (deathCount <= 0)
            baseX = DeathCountHud.BASE_X;

        guiGraphics.blitSprite(NarakaSprites.STIGMA_BACKGROUND, baseX, baseY, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        for (int i = 0; i < stigma; i++) {
            int x = baseX + STIGMA_START_X + i * (STIGMA_OFFSET_BORDER + STIGMA_SIZE);
            int y = baseY + STIGMA_START_Y;
            guiGraphics.blitSprite(NarakaSprites.STIGMA, x, y, STIGMA_SIZE, STIGMA_SIZE);
        }
    }
}

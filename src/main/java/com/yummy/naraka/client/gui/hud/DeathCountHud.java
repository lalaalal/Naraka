package com.yummy.naraka.client.gui.hud;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class DeathCountHud implements HudRenderCallback {
    private static final ResourceLocation BACKGROUND = NarakaMod.location("hud/death_count_background");
    private static final ResourceLocation HEART = NarakaMod.location("hud/death_count_heart");

    public static final int HEART_WIDTH = 14;
    public static final int HEART_HEIGHT = 7;
    public static final int BACKGROUND_WIDTH = 75;
    public static final int BACKGROUND_HEIGHT = 27;
    public static final int HEART_PURPLE_X = 0;
    public static final int HEART_PINK_X = 7;
    public static final int HEART_SIZE_SINGLE = 7;
    public static final int HEART_GAP = 4;
    public static final int HEART_START_X = 6;
    public static final int HEART_START_Y = 8;
    public static final int HEART_OFFSET_BORDER = 2;

    public static final int BASE_X = HEART_SIZE_SINGLE;
    public static final int BASE_Y = HEART_SIZE_SINGLE;

    public static final int BLINKING_TIME = 60;

    private int prevDeathCount;
    private int blinkTime;

    private static void drawHeart(GuiGraphics guiGraphics, int x, int y, boolean fill, boolean blink) {
        if (fill) {
            int u = blink ? HEART_PURPLE_X : HEART_PINK_X;
            guiGraphics.blitSprite(HEART, HEART_WIDTH, HEART_HEIGHT,
                    u, 0, x, y,
                    HEART_SIZE_SINGLE, HEART_SIZE_SINGLE);
        }
    }

    @Override
    public void onHudRender(GuiGraphics guiGraphics, DeltaTracker tickCounter) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        int deathCount = DeathCountHelper.get(player);
        if (deathCount <= 0)
            return;

        if (deathCount < prevDeathCount)
            blinkTime = BLINKING_TIME;
        boolean blink = (blinkTime / 10) % 2 == 0;
        guiGraphics.blitSprite(BACKGROUND, BASE_X, BASE_Y, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        for (int i = 0; i < DeathCountHelper.MAX_DEATH_COUNT; i++) {
            int x = HEART_START_X + HEART_OFFSET_BORDER + BASE_X + i * (HEART_SIZE_SINGLE + HEART_GAP + HEART_OFFSET_BORDER);
            int y = BASE_Y + HEART_START_Y + HEART_OFFSET_BORDER;
            boolean fill = i < deathCount;
            drawHeart(guiGraphics, x, y, fill, blink);
        }

        prevDeathCount = deathCount;
        if (blinkTime > 0)
            blinkTime--;
    }
}

package com.yummy.naraka.gui.layer;

import com.mojang.logging.LogUtils;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.attachment.DeathCountHelper;
import com.yummy.naraka.attachment.NarakaAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Draw player's death count
 *
 * @see NarakaAttachments#DEATH_COUNT
 * @see DeathCountHelper
 *
 * @author lalaalal
 */
@OnlyIn(Dist.CLIENT)
public class DeathCountLayer implements LayeredDraw.Layer {
    private static final ResourceLocation DEATH_COUNT = NarakaMod.location("hud/death_count");
    private static final int HEART_SIZE = 9;
    private static final int SPRITE_WIDTH = HEART_SIZE * 6;
    private static final int SPRITE_HEIGHT = HEART_SIZE;

    private static final int FILLED = HEART_SIZE * 4;
    private static final int PINK = HEART_SIZE * 5;
    private static final int FILLED_OUTLINE = HEART_SIZE * 2;
    private static final int OUTLINE_WHITE = HEART_SIZE;
    private static final int OUTLINE_BLACK = 0;
    private static final Logger log = LoggerFactory.getLogger(DeathCountLayer.class);

    private static void drawFilledHeart(GuiGraphics guiGraphics, int x, int y, boolean fill) {
        guiGraphics.blitSprite(DEATH_COUNT, SPRITE_WIDTH, SPRITE_HEIGHT,
                FILLED_OUTLINE, 0, x, y,
                HEART_SIZE, HEART_SIZE);
        if (fill)
            guiGraphics.blitSprite(DEATH_COUNT, SPRITE_WIDTH, SPRITE_HEIGHT,
                    FILLED, 0, x, y,
                    HEART_SIZE, HEART_SIZE);
    }

    @Override
    public void render(GuiGraphics guiGraphics, float partialTick) {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        int deathCount = DeathCountHelper.getDeathCount(player);

        int baseX = guiGraphics.guiWidth() / 2 - HEART_SIZE * (DeathCountHelper.DEFAULT_DEATH_COUNT + 1);
        int baseY = HEART_SIZE * 2;

        int boxMinX = baseX - HEART_SIZE;
        int boxMinY = baseY - HEART_SIZE;
        int boxMaxX = baseX + HEART_SIZE * (DeathCountHelper.DEFAULT_DEATH_COUNT + 1);
        int boxMaxY = baseY + HEART_SIZE * 2;
        guiGraphics.fill(RenderType.guiOverlay(), boxMinX, boxMinY, boxMaxX, boxMaxY, -0xAAFF0000);

        for (int i = 0; i < DeathCountHelper.DEFAULT_DEATH_COUNT; i++) {
            int x = baseX + i * HEART_SIZE;
            boolean fill = i < deathCount;
            drawFilledHeart(guiGraphics, x, baseY, fill);
        }
    }
}

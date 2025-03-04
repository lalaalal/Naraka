package com.yummy.naraka.client.gui.hud;

import com.yummy.naraka.client.NarakaSprites;
import com.yummy.naraka.world.entity.data.*;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class DeathCountHud implements ClientGuiEvent.RenderHud {
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

    public static final int DEATH_WIDTH = 150;
    public static final int DEATH_HEIGHT = 150;

    public static final int BASE_X = HEART_SIZE_SINGLE;
    public static final int BASE_Y = HEART_SIZE_SINGLE;

    public static final int BLINKING_TIME = 60;
    public static final int STIGMA_DISPLAYING_TIME = 60;

    private int blinkTime;
    private int stigmaDisplayTick;

    private static boolean isCurrentPlayer(LivingEntity livingEntity) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            throw new IllegalStateException("Player is not set");
        return player.getUUID().equals(livingEntity.getUUID());
    }

    private static void drawHeart(GuiGraphics guiGraphics, int x, int y, boolean fill, boolean blink) {
        if (fill) {
            int u = blink ? HEART_PURPLE_X : HEART_PINK_X;
            guiGraphics.blitSprite(NarakaSprites.DEATH_COUNT_HEART, HEART_WIDTH, HEART_HEIGHT,
                    u, 0, x, y,
                    HEART_SIZE_SINGLE, HEART_SIZE_SINGLE);
        }
    }

    public DeathCountHud() {
        EntityDataHelper.registerDataChangeListener(NarakaEntityDataTypes.DEATH_COUNT.get(), this::onDeathCountChanged);
    }

    private void onDeathCountChanged(LivingEntity livingEntity, EntityDataType<Integer> entityDataType, Integer from, Integer to) {
        if (isCurrentPlayer(livingEntity)) {
            if (to < from && to < DeathCountHelper.MAX_DEATH_COUNT)
                blinkTime = BLINKING_TIME;
        }
    }

    private void onStigmaConsumed(LivingEntity livingEntity, EntityDataType<Stigma> entityDataType, Stigma from, Stigma to) {
        if (isCurrentPlayer(livingEntity)) {
            if (from.value() == Stigma.MAX_STIGMA && to.value() == 0)
                stigmaDisplayTick = STIGMA_DISPLAYING_TIME;
        }
    }

    private void renderStigmaIcon(GuiGraphics guiGraphics) {
        int x = guiGraphics.guiWidth() / 2 - DEATH_WIDTH / 2;
        int y = guiGraphics.guiHeight() / 2 - DEATH_HEIGHT / 2;

        float alpha = blinkTime / (float) BLINKING_TIME;
        TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(NarakaSprites.STIGMA_CONSUME);
        guiGraphics.blit(x, y, 0, DEATH_WIDTH, DEATH_HEIGHT, sprite, 1, 1, 1, alpha);
    }

    @Override
    public void renderHud(GuiGraphics guiGraphics, DeltaTracker tickCounter) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        int deathCount = DeathCountHelper.get(player);
        if (deathCount <= 0)
            return;

        boolean blink = (blinkTime / 10) % 2 == 0;
        guiGraphics.blitSprite(NarakaSprites.DEATH_COUNT_BACKGROUND, BASE_X, BASE_Y, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        for (int i = 0; i < DeathCountHelper.MAX_DEATH_COUNT; i++) {
            int x = HEART_START_X + HEART_OFFSET_BORDER + BASE_X + i * (HEART_SIZE_SINGLE + HEART_GAP + HEART_OFFSET_BORDER);
            int y = BASE_Y + HEART_START_Y + HEART_OFFSET_BORDER;
            boolean fill = i < deathCount;
            drawHeart(guiGraphics, x, y, fill, blink);
        }

        if (blinkTime > 0)
            blinkTime--;

        if (stigmaDisplayTick > 0) {
            renderStigmaIcon(guiGraphics);
            stigmaDisplayTick--;
        }
    }
}

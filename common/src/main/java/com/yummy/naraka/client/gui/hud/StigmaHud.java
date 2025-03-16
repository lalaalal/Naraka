package com.yummy.naraka.client.gui.hud;

import com.yummy.naraka.NarakaMod;
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
public class StigmaHud implements ClientGuiEvent.RenderHud {
    public static final int BACKGROUND_WIDTH = 16;
    public static final int BACKGROUND_HEIGHT = 21;
    public static final int STIGMA_SIZE = 2;
    public static final int STIGMA_START_X = 3;
    public static final int STIGMA_START_Y = 18;
    public static final int STIGMA_OFFSET_BORDER = 2;

    public static final int CONSUME_ICON_WIDTH = 150;
    public static final int CONSUME_ICON_HEIGHT = 150;

    public static final int CONSUME_ICON_DISPLAYING_TIME = 60;

    private int consumeIconDisplayTick;

    private static boolean isCurrentPlayer(LivingEntity livingEntity) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            throw new IllegalStateException("Player is not set");
        return player.getUUID().equals(livingEntity.getUUID());
    }

    public StigmaHud() {
        EntityDataHelper.registerDataChangeListener(NarakaEntityDataTypes.STIGMA.get(), this::onStigmaConsumed);
    }

    private void onStigmaConsumed(LivingEntity livingEntity, EntityDataType<Stigma> entityDataType, Stigma from, Stigma to) {
        if (isCurrentPlayer(livingEntity)) {
            if (0 < from.value() && to.value() == 0 && to.lastMarkedTime() != 0)
                consumeIconDisplayTick = CONSUME_ICON_DISPLAYING_TIME;
        }
    }

    private void renderStigmaConsumeIcon(GuiGraphics guiGraphics) {
        int x = guiGraphics.guiWidth() / 2 - CONSUME_ICON_WIDTH / 2;
        int y = guiGraphics.guiHeight() / 2 - CONSUME_ICON_HEIGHT / 2;

        float alpha = consumeIconDisplayTick / (float) CONSUME_ICON_DISPLAYING_TIME;
        TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(NarakaSprites.STIGMA_CONSUME);
        guiGraphics.blit(x, y, 0, CONSUME_ICON_WIDTH, CONSUME_ICON_HEIGHT, sprite, 1, 1, 1, alpha);
    }

    @Override
    public void renderHud(GuiGraphics guiGraphics, DeltaTracker tickCounter) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        final int herobrineTakingStigmaTick = NarakaMod.config().herobrineTakingStigmaTick.getValue();

        Stigma stigma = StigmaHelper.get(player);
        long stigmatizedTime = player.level().getGameTime() - stigma.lastMarkedTime();

        int baseX = DeathCountHud.BASE_X + DeathCountHud.BACKGROUND_WIDTH + 8;
        int baseY = DeathCountHud.BASE_Y + (DeathCountHud.BACKGROUND_HEIGHT - BACKGROUND_HEIGHT) / 2;

        int deathCount = DeathCountHelper.get(player);

        if (consumeIconDisplayTick > 0) {
            renderStigmaConsumeIcon(guiGraphics);
            consumeIconDisplayTick -= 1;
        }

        if (deathCount <= 0 && stigma.value() < 1)
            return;
        if (deathCount <= 0)
            baseX = DeathCountHud.BASE_X;

        if (stigma.lastMarkedTime() != 0 && stigmatizedTime > herobrineTakingStigmaTick / 6 * 5)
            baseX += (int) (stigmatizedTime % 4 / 2) * 2 - 1;

        guiGraphics.blitSprite(NarakaSprites.STIGMA_BACKGROUND, baseX, baseY, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        for (int i = 0; i < stigma.value(); i++) {
            int x = baseX + STIGMA_START_X + i * (STIGMA_OFFSET_BORDER + STIGMA_SIZE);
            int y = baseY + STIGMA_START_Y;
            guiGraphics.blitSprite(NarakaSprites.STIGMA, x, y, STIGMA_SIZE, STIGMA_SIZE);
        }
    }
}

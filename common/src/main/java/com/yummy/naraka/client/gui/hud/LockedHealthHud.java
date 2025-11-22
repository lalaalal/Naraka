package com.yummy.naraka.client.gui.hud;

import com.yummy.naraka.client.NarakaSprites;
import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.EntityDataType;
import com.yummy.naraka.world.entity.data.LockedHealthHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class LockedHealthHud implements LayeredDraw.Layer {
    public static final int HEARTS_PER_LINE = 10;
    public static final int HEART_WIDTH = 8;

    private int blinkTime;

    public static int modifyOffsetHeartIndex(Player player, int tickCount, int original) {
        if (original == -1)
            return original;

        double heartCount = player.getMaxHealth() / 2;
        int offsetHeartIndex = tickCount % Mth.ceil(heartCount + 5.0F);
        if (offsetHeartIndex > heartCount)
            return -1;
        return offsetHeartIndex;
    }

    public static int modifyHeartY(Player player, int heartY, int baseY, int height, int lineCount, int heartIndex) {
        int originalHeartCount = Mth.ceil(player.getMaxHealth() + LockedHealthHelper.get(player) / 2);
        int heartCount = Mth.ceil(player.getMaxHealth() / 2);
        if (heartCount <= heartIndex && heartIndex < originalHeartCount)
            return baseY - lineCount * height;
        return heartY;
    }

    public LockedHealthHud() {
        EntityDataHelper.registerDataChangeListener(NarakaEntityDataTypes.LOCKED_HEALTH.get(), this::onLockedHealthChanged);
        ClientEvents.TICK_PRE.register(this::tick);
    }

    private void onLockedHealthChanged(LivingEntity entity, EntityDataType<Double, LivingEntity> entityDataType, double from, double to) {
        if (NarakaRenderUtils.isCurrentPlayer(entity) && to > from) {
            blinkTime = 20;
        }
    }

    private void tick(Minecraft minecraft) {
        if (blinkTime > 0)
            blinkTime -= 1;
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        if (player == null || !NarakaEntityUtils.isDamageablePlayer(player))
            return;

        int baseX = graphics.guiWidth() / 2 - 91;
        int baseY = graphics.guiHeight() - 39;
        double lockedHealth = EntityDataHelper.getRawEntityData(player, NarakaEntityDataTypes.LOCKED_HEALTH.get());
        double maxHealth = player.getAttributeValue(Attributes.MAX_HEALTH);
        double originalMaxHealth = maxHealth + lockedHealth;
        int heartCount = Mth.ceil(originalMaxHealth / 2);
        int lockedHeartCount = heartCount - (int) Math.round(maxHealth / 2);
        float absorption = player.getAbsorptionAmount();

        boolean hasRightHalfLockedHeart = Mth.ceil(maxHealth) % 2 != 0;
        boolean hasLeftHalfLockedHeart = Mth.ceil(originalMaxHealth) % 2 != 0;

        AttributeInstance attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
        if (attributeInstance == null)
            return;
        AttributeModifier modifier = attributeInstance.getModifier(NarakaAttributeModifiers.REDUCE_MAX_HEALTH_ID);
        if (modifier == null || modifier.amount() != -lockedHealth)
            return;

        int totalHeartLineCount = Mth.ceil((originalMaxHealth + absorption) / 20f);
        int height = Math.max(10 - (totalHeartLineCount - 2), 3);
        boolean blink = (blinkTime / 3) % 2 != 0;
        for (int i = 1; i <= lockedHeartCount; i++) {
            int row = (heartCount - i) / HEARTS_PER_LINE;
            int column = (heartCount - i) % HEARTS_PER_LINE;
            int x = baseX + column * HEART_WIDTH;
            int y = baseY - row * height;
            if (hasRightHalfLockedHeart && i == lockedHeartCount) {
                renderRightHalfLockedHeart(graphics, x, y, blink);
            } else if (hasLeftHalfLockedHeart && i == 1) {
                renderLeftHalfLockedHeart(graphics, x, y, blink);
            } else {
                renderLockedHeart(graphics, x, y, blink);
            }
        }
    }

    private void renderLockedHeart(GuiGraphics graphics, int x, int y, boolean blink) {
        int u = blink ? 7 : 0;
        graphics.blitSprite(NarakaSprites.DEATH_COUNT_HEART, 14, 7, u, 0, x + 1, y + 1, 7, 7);
    }

    private void renderRightHalfLockedHeart(GuiGraphics graphics, int x, int y, boolean blink) {
        int u = blink ? 7 : 0;
        graphics.blitSprite(NarakaSprites.DEATH_COUNT_HEART, 14, 7, u + 4, 0, x + 5, y + 1, 3, 7);
    }

    private void renderLeftHalfLockedHeart(GuiGraphics graphics, int x, int y, boolean blink) {
        int u = blink ? 7 : 0;
        graphics.blitSprite(NarakaSprites.DEATH_COUNT_HEART, 14, 7, u, 0, x + 1, y + 1, 4, 7);
    }
}

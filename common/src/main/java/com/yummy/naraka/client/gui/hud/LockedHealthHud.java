package com.yummy.naraka.client.gui.hud;

import com.yummy.naraka.client.NarakaSprites;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class LockedHealthHud implements ClientGuiEvent.RenderHud {
    public static final int HEARTS_PER_LINE = 10;
    public static final int HEART_WIDTH = 8;
    public static final int HEART_HEIGHT = 9;

    @Override
    public void renderHud(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || player.isCreative())
            return;

        int heartBaseX = graphics.guiWidth() / 2 - 91;
        int heartBaseY = graphics.guiHeight() - 39;
        double lockedHealth = EntityDataHelper.getEntityData(player, NarakaEntityDataTypes.LOCKED_HEALTH.get());
        double originalMaxHealth = player.getMaxHealth() + lockedHealth;
        int heartCount = Mth.ceil(originalMaxHealth / 2);
        int lockedHeartCount = Mth.ceil(lockedHealth / 2);

        AttributeInstance attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
        if (attributeInstance == null)
            return;
        AttributeModifier modifier = attributeInstance.getModifier(NarakaAttributeModifiers.REDUCE_MAX_HEALTH_ID);
        if (modifier == null || modifier.amount() != -lockedHealth)
            return;

        for (int i = 1; i <= lockedHeartCount; i++) {
            int row = (heartCount - i) / HEARTS_PER_LINE;
            int column = (heartCount - i) % HEARTS_PER_LINE;
            int x = heartBaseX + column * HEART_WIDTH;
            int y = heartBaseY - row * HEART_HEIGHT;
            renderLockedHeart(graphics, x, y);
        }
    }

    public void renderLockedHeart(GuiGraphics graphics, int x, int y) {
        graphics.blitSprite(NarakaSprites.HEART_CONTAINER, x, y, 9, 9);
        graphics.blitSprite(NarakaSprites.DEATH_COUNT_HEART, 14, 7, 0, 0, x + 1, y + 1, 7, 7);
    }
}

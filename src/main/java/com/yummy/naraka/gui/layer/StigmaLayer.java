package com.yummy.naraka.gui.layer;

import com.yummy.naraka.attachment.NarakaAttachments;
import com.yummy.naraka.attachment.StigmaHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Draw player's stigma
 *
 * @see NarakaAttachments#STIGMA
 * @see StigmaHelper
 *
 * @author lalaalal
 */
@OnlyIn(Dist.CLIENT)
public class StigmaLayer implements LayeredDraw.Layer {
    public static final ResourceLocation STIGMA_SPRITE = new ResourceLocation("hud/food_empty");

    @Override
    public void render(GuiGraphics guiGraphics, float partialTick) {
        Gui gui = Minecraft.getInstance().gui;
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator())
            return;
        int stigma = StigmaHelper.getStigma(player);
        int baseX = guiGraphics.guiWidth() / 2 + 91;
        int baseY = guiGraphics.guiHeight() - gui.rightHeight;

        for (int i = 0; i < stigma; i++) {
            int x = baseX - 8 * i - 9;
            guiGraphics.blitSprite(STIGMA_SPRITE, x, baseY, 9, 9);
        }
    }
}

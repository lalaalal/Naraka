package com.yummy.naraka.client.gui.screen;

import com.yummy.naraka.client.NarakaSprites;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.world.inventory.SoulCraftingMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class SoulCraftingScreen extends AbstractContainerScreen<SoulCraftingMenu> {
    public static final int CRAFTING_PROGRESS_WIDTH = 24;
    public static final int CRAFTING_PROGRESS_HEIGHT = 16;

    public static final int LIT_PROGRESS_WIDTH = 14;
    public static final int LIT_PROGRESS_HEIGHT = 14;

    public static final int FUEL_CHARGE_WIDTH = 18;
    public static final int FUEL_CHARGE_HEIGHT = 4;

    public SoulCraftingScreen(SoulCraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(NarakaTextures.SOUL_CRAFTING, x, y, 0, 0, imageWidth, imageHeight);

        int craftingProgress = Mth.ceil(menu.getCraftingProgress() * CRAFTING_PROGRESS_WIDTH);
        guiGraphics.blitSprite(NarakaSprites.SOUL_CRAFTING_PROGRESS, CRAFTING_PROGRESS_WIDTH, CRAFTING_PROGRESS_HEIGHT, 0, 0, leftPos + 79, topPos + 34, craftingProgress, CRAFTING_PROGRESS_HEIGHT);

        int litProgress = Mth.ceil(menu.getLitProgress() * LIT_PROGRESS_HEIGHT);
        if (menu.isCrafting())
            guiGraphics.blitSprite(
                    NarakaSprites.SOUL_CRAFTING_LIT_PROGRESS, LIT_PROGRESS_WIDTH, LIT_PROGRESS_HEIGHT,
                    0, LIT_PROGRESS_HEIGHT - litProgress, leftPos + 57, topPos + 37 + LIT_PROGRESS_HEIGHT - litProgress,
                    LIT_PROGRESS_WIDTH, litProgress
            );

        int fuelCharge = Mth.ceil(menu.getFuelCharge() * FUEL_CHARGE_WIDTH);
        guiGraphics.blitSprite(NarakaSprites.SOUL_CRAFTING_FUEL_CHARGE, FUEL_CHARGE_WIDTH, FUEL_CHARGE_HEIGHT, 0, 0, leftPos + 55, topPos + 53, fuelCharge, FUEL_CHARGE_HEIGHT);
    }
}

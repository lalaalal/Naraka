package com.yummy.naraka.client.gui.screen;

import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.world.inventory.SoulCraftingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SoulCraftingScreen extends AbstractContainerScreen<SoulCraftingMenu> {
    public SoulCraftingScreen(SoulCraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(NarakaTextures.SOUL_CRAFTING, x, y, 0, 0, imageWidth, imageHeight);
    }
}

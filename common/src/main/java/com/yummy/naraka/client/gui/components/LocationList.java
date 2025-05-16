package com.yummy.naraka.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class LocationList extends ObjectSelectionList<LocationList.Entry> {
    private final Screen screen;

    public LocationList(Minecraft minecraft, Screen screen, Set<ResourceLocation> locations) {
        super(minecraft, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight() - 50, 38, 18);
        this.screen = screen;
        for (ResourceLocation location : locations) {
            Entry entry = new Entry(location);
            addEntry(entry);
            setSelected(entry);
        }
    }

    public class Entry extends ObjectSelectionList.Entry<Entry> {
        public final ResourceLocation location;
        private final Component component;

        protected Entry(ResourceLocation location) {
            this.location = location;
            this.component = Component.literal(location.toString());
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            Font font = minecraft.font;
            int entryWidth = screen.width / 2;
            int entryHeight = top + height / 2;
            guiGraphics.drawCenteredString(font, component, entryWidth, entryHeight - 9 / 2, -1);
        }

        @Override
        public Component getNarration() {
            return component;
        }
    }
}

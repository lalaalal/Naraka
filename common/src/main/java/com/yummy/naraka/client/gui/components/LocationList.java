package com.yummy.naraka.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class LocationList extends ObjectSelectionList<LocationList.Entry> {
    private final Screen screen;
    private final Function<Identifier, String> translationKeyGenerator;

    public LocationList(Minecraft minecraft, Screen screen, Set<Identifier> locations, Function<Identifier, String> keyGenerator) {
        super(minecraft, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight() - 50, 38, 18);
        this.screen = screen;
        this.translationKeyGenerator = keyGenerator;
        locations.stream()
                .map(Entry::new)
                .sorted(Comparator.comparing(entry -> entry.component.getString()))
                .forEach(this::addEntry);
        setSelected(children().getFirst());
    }

    public class Entry extends ObjectSelectionList.Entry<Entry> {
        public final Identifier location;
        private final Component component;

        protected Entry(Identifier location) {
            this.location = location;
            this.component = Component.translatable(translationKeyGenerator.apply(location));
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            Font font = minecraft.font;
            int entryWidth = screen.width / 2;
            int entryHeight = getContentY() + getContentHeight() / 2;
            guiGraphics.drawCenteredString(font, component, entryWidth, entryHeight - 9 / 2, -1);
        }

        @Override
        public Component getNarration() {
            return component;
        }
    }
}

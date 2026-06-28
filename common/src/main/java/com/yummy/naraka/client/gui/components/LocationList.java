package com.yummy.naraka.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class LocationList extends ObjectSelectionList<LocationList.Entry> implements LayoutElement {
    private final Screen screen;
    private final Function<ResourceLocation, String> translationKeyGenerator;

    public LocationList(Minecraft minecraft, Screen screen, Set<ResourceLocation> locations, Function<ResourceLocation, String> keyGenerator) {

        super(minecraft, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight() - 50, 38, 38, 18);
        this.screen = screen;
        this.translationKeyGenerator = keyGenerator;
        locations.stream()
                .map(Entry::new)
                .sorted(Comparator.comparing(entry -> entry.component.getString()))
                .forEach(this::addEntry);
        setSelected(children().get(0));
    }

    @Override
    public void setX(int x) {
        this.x0 = x;
    }

    @Override
    public void setY(int y) {
        this.y0 = y;
    }

    @Override
    public int getX() {
        return x0;
    }

    @Override
    public int getY() {
        return y0;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {

    }

    public class Entry extends ObjectSelectionList.Entry<Entry> {
        public final ResourceLocation location;
        private final Component component;

        protected Entry(ResourceLocation location) {
            this.location = location;
            this.component = Component.translatable(translationKeyGenerator.apply(location));
        }

        @Override
        public Component getNarration() {
            return component;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            Font font = minecraft.font;
            int entryWidth = screen.width / 2;
            int entryHeight = top + height / 2;
            guiGraphics.drawCenteredString(font, component, entryWidth, entryHeight - 9 / 2, -1);
        }
    }
}

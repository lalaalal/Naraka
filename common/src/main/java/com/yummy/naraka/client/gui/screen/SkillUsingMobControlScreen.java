package com.yummy.naraka.client.gui.screen;

import com.yummy.naraka.client.gui.components.LocationList;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SkillRequestPayload;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

@Environment(EnvType.CLIENT)
public abstract class SkillUsingMobControlScreen extends Screen {
    protected final SkillUsingMob mob;
    protected final LocationList locationList;
    protected final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    protected SkillUsingMobControlScreen(SkillUsingMob mob, Set<ResourceLocation> locations) {
        super(Component.literal("Skill Control"));
        this.mob = mob;
        this.locationList = new LocationList(Minecraft.getInstance(), this, locations);
    }

    @Override
    protected void init() {
        layout.visitWidgets(this::addRenderableWidget);
        layout.addToContents(locationList);
        LinearLayout linearLayout = new LinearLayout(layout.getWidth(), layout.getFooterHeight(), LinearLayout.Orientation.HORIZONTAL);
        linearLayout.addChild(Button.builder(Component.literal("disable skills"), this::disableSkills).build());
        linearLayout.addChild(Button.builder(CommonComponents.GUI_DONE, this::onDone).build());
        layout.addToFooter(linearLayout);
        layout.arrangeElements();
        layout.visitWidgets(this::addRenderableWidget);
    }

    private void disableSkills(Button button) {
        if (minecraft != null)
            minecraft.setScreen(null);
        SkillRequestPayload payload = new SkillRequestPayload(SkillRequestPayload.Event.DISABLE, mob);
        NetworkManager.serverbound().send(payload);
    }

    private void onDone(Button button) {
        if (minecraft != null)
            minecraft.setScreen(null);
        LocationList.Entry entry = locationList.getSelected();
        if (entry != null)
            select(entry);
    }

    protected abstract void select(LocationList.Entry selected);

    @Override
    protected void repositionElements() {
        super.repositionElements();
        layout.arrangeElements();
        locationList.updateSize(width, layout);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

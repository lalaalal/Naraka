package com.yummy.naraka.client.gui.screen;

import com.yummy.naraka.client.gui.components.LocationList;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SkillRequestPacket;
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
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public abstract class SkillUsingMobControlScreen extends Screen {
    protected final SkillUsingMob mob;
    protected final LocationList locationList;
    protected final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    protected SkillUsingMobControlScreen(SkillUsingMob mob, Set<ResourceLocation> locations, Function<ResourceLocation, String> translationKeyGenerator) {
        super(Component.literal("Skill Control"));
        this.mob = mob;
        this.locationList = new LocationList(Minecraft.getInstance(), this, locations, translationKeyGenerator);
    }

    @Override
    protected void init() {
        layout.addToContents(locationList);
        layout.setHeaderHeight(10);
        layout.setFooterHeight(45);

        LinearLayout footerLayout = new LinearLayout(layout.getWidth(), layout.getFooterHeight(), LinearLayout.Orientation.VERTICAL);
        LinearLayout firstLayout = new LinearLayout(layout.getWidth(), layout.getFooterHeight(), LinearLayout.Orientation.HORIZONTAL);
        firstLayout.addChild(Button.builder(Component.literal("enable selected only"), action(SkillRequestPacket.Event.ENABLE_ONLY)).build());
        firstLayout.addChild(Button.builder(Component.literal("stop current skill"), action(SkillRequestPacket.Event.STOP)).build());

        LinearLayout secondLayout = new LinearLayout(layout.getWidth(), layout.getFooterHeight(), LinearLayout.Orientation.HORIZONTAL);
        secondLayout.addChild(Button.builder(Component.translatable(LanguageKey.DISABLE_SKILL_USE_KEY), this::disableSkills).build());
        secondLayout.addChild(Button.builder(CommonComponents.GUI_DONE, this::onDone).build());

        footerLayout.addChild(firstLayout);
        footerLayout.addChild(secondLayout);
        layout.addToFooter(footerLayout);
        layout.arrangeElements();
        layout.visitWidgets(this::addRenderableWidget);
    }

    private void disableSkills(Button button) {
        if (minecraft != null)
            minecraft.setScreen(null);
        SkillRequestPacket payload = new SkillRequestPacket(SkillRequestPacket.Event.DISABLE, mob);
        NetworkManager.serverbound().send(payload);
    }

    private void onDone(Button button) {
        if (minecraft != null)
            minecraft.setScreen(null);
        LocationList.Entry entry = locationList.getSelected();
        if (entry != null)
            select(entry);
    }

    private Button.OnPress action(SkillRequestPacket.Event event) {
        return button -> {
            if (minecraft != null)
                minecraft.setScreen(null);
            if (locationList.getSelected() == null)
                return;
            SkillRequestPacket payload = new SkillRequestPacket(event, mob, locationList.getSelected().location);
            NetworkManager.serverbound().send(payload);
        };
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

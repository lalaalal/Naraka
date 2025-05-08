package com.yummy.naraka.client.gui.screen;

import com.yummy.naraka.client.gui.components.SkillList;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SkillRequestPayload;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SkillControlScreen extends Screen {
    private final SkillUsingMob mob;
    private final SkillList skillList;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public SkillControlScreen(SkillUsingMob mob) {
        super(Component.literal("Skill Control"));
        this.mob = mob;
        SkillManager skillManager = mob.getSkillManager();
        this.skillList = new SkillList(Minecraft.getInstance(), this, skillManager.getSkillNames());
    }

    @Override
    protected void init() {
        layout.visitWidgets(this::addRenderableWidget);
        layout.addToContents(skillList);
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
        SkillList.Entry entry = skillList.getSelected();
        if (entry != null) {
            SkillRequestPayload payload = new SkillRequestPayload(SkillRequestPayload.Event.USE, mob, entry.location);
            NetworkManager.serverbound().send(payload);
        }
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
        layout.arrangeElements();
        skillList.updateSize(width, layout);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

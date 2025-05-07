package com.yummy.naraka.client.gui.screen;

import com.yummy.naraka.client.gui.components.SkillList;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SkillRequestPayload;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
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
        this.skillList = new SkillList(Minecraft.getInstance(), this, mob.getSkillNames());
    }

    @Override
    protected void init() {
        layout.visitWidgets(this::addRenderableWidget);
        layout.addToContents(skillList);
        layout.addToFooter(
                Button.builder(CommonComponents.GUI_DONE, this::onDone)
                        .build()
        );
        layout.arrangeElements();
        layout.visitWidgets(this::addRenderableWidget);
    }

    private void onDone(Button button) {
        assert minecraft != null;
        minecraft.setScreen(null);
        SkillList.Entry entry = skillList.getSelected();
        if (entry != null) {
            SkillRequestPayload payload = new SkillRequestPayload(mob, entry.location);
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

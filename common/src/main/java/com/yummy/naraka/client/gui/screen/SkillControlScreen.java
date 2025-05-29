package com.yummy.naraka.client.gui.screen;

import com.yummy.naraka.client.gui.components.LocationList;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SkillRequestPayload;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SkillControlScreen extends SkillUsingMobControlScreen {
    public SkillControlScreen(SkillUsingMob mob) {
        super(mob, mob.getSkillManager().getSkillNames());
    }

    @Override
    protected void select(LocationList.Entry selected) {
        SkillRequestPayload payload = new SkillRequestPayload(SkillRequestPayload.Event.USE, mob, selected.location);
        NetworkManager.serverbound().send(payload);
    }
}

package com.yummy.naraka.client.gui.screen;

import com.yummy.naraka.client.gui.components.LocationList;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AnimationControlScreen extends SkillUsingMobControlScreen {
    public AnimationControlScreen(SkillUsingMob mob) {
        super(mob, mob.getAnimations());
    }

    @Override
    protected void select(LocationList.Entry selected) {
        mob.updateAnimation(selected.location);
    }
}

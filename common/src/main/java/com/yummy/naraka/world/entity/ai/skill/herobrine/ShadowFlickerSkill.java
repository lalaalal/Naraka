package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.server.level.ServerLevel;

public class ShadowFlickerSkill extends FlickerSkill<ShadowHerobrine> {
    public ShadowFlickerSkill(ShadowHerobrine mob, DashSkill<?> dashSkill, PunchSkill<?> punchSkill) {
        super(mob, dashSkill, punchSkill);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return super.canUse(level) && mob.otherShadowNotUsingSkill(level);
    }
}

package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

public class SummonShadowSkill extends Skill<Herobrine> {
    public static final Identifier LOCATION = createLocation("herobrine.summon_shadow");

    public SummonShadowSkill(Herobrine mob) {
        super(LOCATION, mob, 20, 0);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getShadowCount() == 0;
    }

    @Override
    protected void skillTick(ServerLevel level) {

    }

    @Override
    protected void onLastTick(ServerLevel level) {
        mob.getShadowController().ifPresent(controller -> controller.summonShadowHerobrine(level));
    }
}

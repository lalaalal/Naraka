package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.server.level.ServerLevel;

public class SummonShadowSkill extends Skill<Herobrine> {
    public static final String NAME = "summon_shadow";

    public SummonShadowSkill(Herobrine mob) {
        super(NAME, 20, 0, mob);
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
        mob.getShadowController().summonShadowHerobrine(level);
    }
}

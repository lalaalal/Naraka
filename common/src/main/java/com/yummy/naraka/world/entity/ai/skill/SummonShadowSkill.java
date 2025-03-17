package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.Herobrine;

public class SummonShadowSkill extends Skill<Herobrine> {
    public static final String NAME = "summon_shadow";

    public SummonShadowSkill(Herobrine mob) {
        super(NAME, 20, 0, mob);
    }

    @Override
    public boolean canUse() {
        return mob.getShadowCount() == 0;
    }

    @Override
    protected void skillTick() {

    }

    @Override
    protected void onLastTick() {
        mob.summonShadowHerobrine();
    }
}

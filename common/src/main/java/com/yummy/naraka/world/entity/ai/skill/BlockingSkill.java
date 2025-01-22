package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.world.entity.LivingEntity;

public class BlockingSkill extends Skill {
    public static final String NAME = "blocking";

    public BlockingSkill(SkillUsingMob mob) {
        super(NAME, 20, 0, mob);
    }

    @Override
    public boolean readyToUse() {
        return false;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    protected void skillTick() {

    }

    @Override
    protected void onLastTick() {
        LivingEntity target = mob.getTarget();
        if (target == null)
            return;
        if (mob.distanceToSqr(target) <= 2 * 2)
            target.hurt(mob.damageSources().mobAttack(mob), 4);
    }
}

package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class BlockingSkill extends Skill<SkillUsingMob> {
    public static final String NAME = "blocking";

    public BlockingSkill(SkillUsingMob mob) {
        super(NAME, 30, 0, mob);
    }

    @Override
    public boolean readyToUse() {
        return false;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return true;
    }

    @Override
    protected void skillTick(ServerLevel level) {

    }

    @Override
    protected void onLastTick(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        if (target == null)
            return;
        if (mob.distanceToSqr(target) <= 2 * 2)
            target.hurtServer(level, mob.damageSources().mobAttack(mob), 4);
    }
}

package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DashSkill extends Skill {
    public DashSkill(SkillUsingMob mob) {
        super("dash", 20, 60, mob);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null;
    }

    @Override
    protected void skillTick() {
        mob.getNavigation().stop();
        if (10 <= tickCount && tickCount <= 15)
            mob.createAfterimage();
        if (tickCount == 10) {
            LivingEntity target = mob.getTarget();
            if (target == null)
                return;

            Vec3 delta = target.position().subtract(mob.position()).normalize().scale(4);
            mob.setDeltaMovement(mob.getDeltaMovement().add(delta));
        }

        if (tickCount == 15)
            mob.setDeltaMovement(Vec3.ZERO);
    }
}

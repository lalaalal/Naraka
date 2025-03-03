package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.Afterimage;
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
        return target != null && mob.distanceToSqr(target) > 3 * 3;
    }

    @Override
    protected void onFirstTick() {
        LivingEntity target = mob.getTarget();
        if (target != null)
            mob.lookAt(target, 360, 0);
    }

    @Override
    protected void skillTick() {
        LivingEntity target = mob.getTarget();
        if (target == null)
            return;

        mob.getNavigation().stop();
        Vec3 delta = NarakaEntityUtils.getDirectionNormalVector(mob, target)
                .scale(3);

        if (10 <= tickCount && tickCount <= 15)
            mob.addAfterimage(new Afterimage(mob.position(), 15));
        if (tickCount == 10)
            mob.setDeltaMovement(delta);
        if (tickCount == 15)
            mob.setDeltaMovement(Vec3.ZERO);
    }
}

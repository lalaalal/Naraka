package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DashSkill<T extends SkillUsingMob & AfterimageEntity> extends Skill<T> {
    private Vec3 deltaMovement = Vec3.ZERO;

    public DashSkill(T mob) {
        super("dash", 20, 60, mob);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null;
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

        if (tickCount == 10) {
            this.deltaMovement = NarakaEntityUtils.getDirectionNormalVector(mob, target);
        }
        if (10 <= tickCount && tickCount < 15 && mob.distanceToSqr(target) > 3) {
            mob.setDeltaMovement(deltaMovement);
            mob.addAfterimage(Afterimage.of(mob, 15));
        }
        if (tickCount == 15 || mob.distanceToSqr(target) < 3)
            mob.setDeltaMovement(Vec3.ZERO);
    }
}

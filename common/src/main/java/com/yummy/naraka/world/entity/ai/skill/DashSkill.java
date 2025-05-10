package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DashSkill<T extends SkillUsingMob & AfterimageEntity> extends Skill<T> {
    public static final String NAME = "dash";

    private Vec3 deltaMovement = Vec3.ZERO;

    public DashSkill(T mob) {
        super(NAME, 20, 40, mob);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        return target != null;
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        if (target != null)
            mob.lookAt(target, 360, 0);
    }

    @Override
    protected void skillTick(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        if (target == null)
            return;

        mob.getNavigation().stop();

        if (tickCount == 10)
            this.deltaMovement = NarakaEntityUtils.getDirectionNormalVector(mob, target);
        if (((10 <= tickCount && tickCount <= 15) || hasLinkedSkill()) && mob.distanceToSqr(target) > 3) {
            NarakaEntityUtils.updatePositionForUpStep(level, mob, deltaMovement, 0.6);
            mob.setDeltaMovement(deltaMovement);
            mob.addAfterimage(Afterimage.of(mob, 13), 2, tickCount < 15);
        }
        if (tickCount == 15 || mob.distanceToSqr(target) < 3)
            mob.setDeltaMovement(Vec3.ZERO);
    }
}

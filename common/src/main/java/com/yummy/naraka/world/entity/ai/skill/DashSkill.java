package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DashSkill<T extends SkillUsingMob & AfterimageEntity> extends TargetSkill<T> {
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
        mob.getNavigation().stop();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        rotateTowardTarget(target);

        runAt(10, () -> this.deltaMovement = NarakaEntityUtils.getDirectionNormalVector(mob, target).multiply(1, 0, 1));
        if (hasLinkedSkill())
            this.deltaMovement = deltaMovement.scale(1.2);
        run(and(between(10, 15), t -> mob.distanceToSqr(target) > 3), () -> move(level));
        run(or(at(15), t -> mob.distanceToSqr(target) < 3), () -> mob.setDeltaMovement(Vec3.ZERO));
    }

    private void move(ServerLevel level) {
        NarakaEntityUtils.updatePositionForUpStep(level, mob, deltaMovement, 0.6);
        mob.setDeltaMovement(deltaMovement);
        mob.addAfterimage(Afterimage.of(mob, 13), 2, tickCount < 15);
    }
}

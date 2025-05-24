package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DashSkill<T extends SkillUsingMob & AfterimageEntity> extends TargetSkill<T> {
    public static final ResourceLocation LOCATION = createLocation("dash");

    private Vec3 deltaMovement = Vec3.ZERO;
    private float scale = 1;

    public DashSkill(T mob) {
        super(LOCATION, 10, 40, mob);
    }

    public void setScale(float scale) {
        this.scale = scale;
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

        runAt(0, () -> updateDeltaMovement(target));
        run(between(0, 5) && mob.distanceToSqr(target) > 3, () -> move(level));
        run(after(4) && hasLinkedSkill(), () -> move(level));
        run((at(5) && !hasLinkedSkill() && scale > 0) || at(duration - 1) || (mob.distanceToSqr(target) < 3 && scale > 0), () -> mob.setDeltaMovement(Vec3.ZERO));
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        scale = 1;
    }

    private void updateDeltaMovement(LivingEntity target) {
        this.deltaMovement = NarakaEntityUtils.getDirectionNormalVector(mob, target)
                .multiply(1, 0, 1)
                .scale(scale);
        if (hasLinkedSkill())
            this.deltaMovement = deltaMovement.scale(1.5);
    }

    private void move(ServerLevel level) {
        NarakaEntityUtils.updatePositionForUpStep(level, mob, deltaMovement, 0.6);
        mob.setDeltaMovement(deltaMovement);
        mob.addAfterimage(Afterimage.of(mob, 13), 1, true);
    }
}

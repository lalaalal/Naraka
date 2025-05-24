package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DashAroundSkill<T extends SkillUsingMob & AfterimageEntity> extends TargetSkill<T> {
    public static final String NAME = "dash_around";
    private Vec3 deltaMovement = Vec3.ZERO;
    private Vec3 previousTargetPosition = Vec3.ZERO;

    public DashAroundSkill(T mob) {
        super(NAME, 15, 40, mob);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetOutOfRange(2);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        rotateTowardTarget(target);
        runAt(2, () -> this.previousTargetPosition = target.position());
        runAt(5, () -> this.updateDeltaMovement(target));
        if (targetInRange(4))
            deltaMovement = Vec3.ZERO;
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runBetween(6, 11, () -> move(level));
        runAt(11, () -> mob.setDeltaMovement(Vec3.ZERO));
    }

    private void move(ServerLevel level) {
        NarakaEntityUtils.updatePositionForUpStep(level, mob, deltaMovement, 0.4);
        mob.setDeltaMovement(deltaMovement);
        if (!deltaMovement.equals(Vec3.ZERO))
            mob.addAfterimage(Afterimage.of(mob, 10), 1, tickCount < 11);
    }

    private void updateDeltaMovement(LivingEntity target) {
        Vec3 delta = NarakaEntityUtils.getDirectionNormalVector(mob, target)
                .multiply(1, 0, 1);
        Vec3 base = delta.yRot(Mth.HALF_PI);

        Vec3 targetMovement = target.position().subtract(previousTargetPosition);
        Vec3 compare = delta.add(targetMovement);
        if (targetMovement.x == 0 && targetMovement.z == 0) {
            int direction = mob.getRandom().nextInt(2);
            deltaMovement = base.yRot(Mth.PI * direction);
        } else {
            Vec3 projection = NarakaUtils.projection(compare, base);
            NarakaMod.LOGGER.info("{}", projection);
            deltaMovement = projection.normalize()
                    .scale(-1);
        }
        deltaMovement = deltaMovement.add(0, -0.9, 0)
                .add(delta.scale(1.5))
                .scale(0.6);
    }
}

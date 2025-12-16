package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.ai.skill.TargetSkill;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DashAroundSkill<T extends SkillUsingMob & AfterimageEntity> extends TargetSkill<T> {
    public static final Identifier LOCATION = createLocation("herobrine.dash_around");
    private Vec3 prevPosition = Vec3.ZERO;
    private Vec3 deltaMovement = Vec3.ZERO;
    private Vec3 previousTargetPosition = Vec3.ZERO;

    public DashAroundSkill(T mob) {
        super(LOCATION, mob, 15, 40);
    }

    @Override
    public void prepare() {
        super.prepare();
        prevPosition = mob.position();
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
        runBetween(6, 11, this::move);
        runAt(11, () -> mob.setDeltaMovement(Vec3.ZERO));
    }

    private void move() {
        mob.setDeltaMovement(deltaMovement);
        double movedDistance = prevPosition.subtract(mob.position()).length();
        if (!deltaMovement.equals(Vec3.ZERO) && movedDistance >= deltaMovement.length() * 0.9)
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
            Vec3 projection = compare.projectedOn(base);
            deltaMovement = projection.normalize()
                    .scale(-1);
        }
        deltaMovement = deltaMovement.add(delta.scale(1.5))
                .scale(0.6);
    }
}

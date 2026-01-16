package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.ai.skill.TargetSkill;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DashSkill<T extends AbstractHerobrine> extends TargetSkill<T> {
    public static final Identifier LOCATION = skillIdentifier("herobrine.dash");

    private Vec3 prevPosition = Vec3.ZERO;
    private Vec3 deltaMovement = Vec3.ZERO;
    private float scale = 1;
    private boolean alwaysMove = false;

    public DashSkill(T mob) {
        super(LOCATION, mob, 10, 40);
    }

    @Override
    public void prepare() {
        super.prepare();
        prevPosition = mob.position();
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
        run(between(0, 10) && (mob.distanceToSqr(target) > 3 || alwaysMove), this::move);
        run((at(9) || (mob.distanceToSqr(target) < 3 && scale > 0)), () -> mob.setDeltaMovement(Vec3.ZERO));
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        scale = 1;
        alwaysMove = false;
    }

    private void updateDeltaMovement(LivingEntity target) {
        this.deltaMovement = NarakaEntityUtils.getDirectionNormalVector(mob, target)
                .multiply(1, 0, 1)
                .scale(scale);
        if (hasLinkedSkill())
            this.deltaMovement = deltaMovement.scale(1.5);
    }

    private void move() {
        mob.setDeltaMovement(deltaMovement);
        double movedDistance = prevPosition.subtract(mob.position()).length();
        if (!deltaMovement.equals(Vec3.ZERO) && movedDistance >= deltaMovement.length() * 0.9)
            mob.addAfterimage(Afterimage.of(mob, 10), 1, tickCount < 9);
        prevPosition = mob.position();
    }
}

package com.yummy.naraka.world.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class WalkAroundTargetGoal extends Goal {
    private final PathfinderMob mob;
    private final int startDistance;
    private final int stopDistance;
    private final float speedModifier;
    private int direction;

    public WalkAroundTargetGoal(PathfinderMob mob, int startDistance, int stopDistance, float speedModifier) {
        this.mob = mob;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && mob.distanceToSqr(target) <= startDistance * startDistance;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = mob.getTarget();
        return canUse() && target != null && mob.distanceToSqr(target) > stopDistance * stopDistance;
    }

    @Override
    public void start() {
        direction = mob.getRandom().nextBoolean() ? 1 : -1;
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        walkAround();
    }

    private void walkAround() {
        LivingEntity target = mob.getTarget();
        if (target == null)
            return;

        Vec3 delta = mob.position().subtract(target.position());
        Vec3 wanted = delta.yRot(Mth.PI / 6 * direction).add(target.position());

        Path path = mob.getNavigation().createPath(wanted.x, wanted.y, wanted.z, 1);
        mob.getNavigation().moveTo(path, speedModifier);
    }
}

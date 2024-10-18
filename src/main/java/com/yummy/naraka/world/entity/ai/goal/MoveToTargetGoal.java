package com.yummy.naraka.world.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class MoveToTargetGoal extends Goal {
    private final PathfinderMob mob;
    @Nullable
    private LivingEntity target;
    private final double speedModifier;
    private final float within;

    public MoveToTargetGoal(PathfinderMob mob, double speedModifier, float within) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.within = within;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target == null)
            return false;
        return this.target.distanceToSqr(this.mob) < (this.within * this.within);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone()
                && this.target != null
                && this.target.isAlive()
                && this.target.distanceToSqr(this.mob) < (this.within * this.within);
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(target, speedModifier);
    }
}

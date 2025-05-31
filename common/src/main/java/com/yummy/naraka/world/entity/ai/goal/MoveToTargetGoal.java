package com.yummy.naraka.world.entity.ai.goal;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class MoveToTargetGoal extends Goal {
    private final AbstractHerobrine mob;
    @Nullable
    private LivingEntity target;
    private final double speedModifier;
    private final float within;
    private final int accuracy;
    private final float foolChance;
    private final int interval;
    private int tickCount = 0;

    public MoveToTargetGoal(AbstractHerobrine mob, double speedModifier, float within, int accuracy, int interval, float foolChange) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.within = within;
        this.accuracy = accuracy;
        this.interval = interval;
        this.foolChance = foolChange;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target == null || mob.isUsingSkill() || mob.isStaggering())
            return false;
        return this.target.distanceToSqr(this.mob) < (this.within * this.within) && this.target.distanceToSqr(this.mob) > 9;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone()
                && tickCount > 0
                && !mob.isUsingSkill()
                && !mob.isStaggering()
                && this.target != null
                && this.target.isAlive()
                && this.target.distanceToSqr(this.mob) < (this.within * this.within)
                && this.target.distanceToSqr(this.mob) > 9;
    }

    @Override
    public void stop() {
        this.target = null;
        mob.getNavigation().stop();
    }

    @Override
    public void start() {
        if (target != null)
            tryMoveToTarget(target);
        tickCount = interval;
    }

    @Override
    public void tick() {
        tickCount -= 1;
    }

    private void tryMoveToTarget(LivingEntity target) {
        Vec3 wanted = getWantedPosition(target);
        Path path = mob.getNavigation().createPath(wanted.x, wanted.y, wanted.z, accuracy);
        mob.getNavigation().moveTo(path, speedModifier);
    }

    private Vec3 getWantedPosition(LivingEntity target) {
        if (mob.getRandom().nextFloat() < foolChance) {
            Vec3 pos = DefaultRandomPos.getPos(this.mob, 10, 7);
            if (pos == null)
                return mob.position();
            return pos;
        }
        return target.position();
    }
}

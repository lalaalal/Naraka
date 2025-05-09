package com.yummy.naraka.world.entity.ai.goal;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class MoveToTargetGoal extends Goal {
    private final SkillUsingMob mob;
    @Nullable
    private LivingEntity target;
    private final double speedModifier;
    private final float within;
    private final int updateTick;
    private int tickCount;
    private final float foolChance;

    public MoveToTargetGoal(SkillUsingMob mob, double speedModifier, float within, int updateTick, float foolChange) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.within = within;
        this.updateTick = updateTick;
        this.foolChance = foolChange;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public MoveToTargetGoal(SkillUsingMob mob, double speedModifier, float within) {
        this(mob, speedModifier, within, 1, 0);
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target == null || this.mob.isUsingSkill())
            return false;
        return this.target.distanceToSqr(this.mob) < (this.within * this.within);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone()
                && !this.mob.isUsingSkill()
                && tickCount > 0
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
        if (target != null)
            tryMoveToTarget(target);

        tickCount = updateTick;
    }

    private void tryMoveToTarget(LivingEntity target) {
        if (foolChance != 0 && foolChance < mob.getRandom().nextFloat()) {
            Vec3 wanted = getWantedPosition();
            mob.getNavigation().moveTo(wanted.x, wanted.y, wanted.z, speedModifier);
        } else {
            mob.getNavigation().moveTo(target, speedModifier);
        }
    }

    private Vec3 getWantedPosition() {
        Vec3 pos = DefaultRandomPos.getPos(this.mob, 10, 7);
        if (pos == null)
            return mob.position();
        return pos;
    }

    @Override
    public void tick() {
        tickCount -= 1;
    }
}

package com.yummy.naraka.world.entity.ai.goal;

import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class FollowOwnerGoal<T extends Mob & TraceableEntity> extends Goal {
    public static final double START_FOLLOWING_DISTANCE = 18;
    public static final double TELEPORT_DISTANCE = 24;

    private final T entity;
    private int timeToRecalculatePath;

    public FollowOwnerGoal(T entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        Entity owner = entity.getOwner();
        return owner != null && entity.distanceToSqr(owner) > START_FOLLOWING_DISTANCE * START_FOLLOWING_DISTANCE;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    public void start() {
        this.timeToRecalculatePath = 0;
    }

    @Override
    public void tick() {
        super.tick();

        Entity owner = entity.getOwner();
        if (owner == null)
            return;
        entity.getLookControl().setLookAt(owner, 10.0F, (float) entity.getMaxHeadXRot());
        if (--this.timeToRecalculatePath <= 0) {
            this.timeToRecalculatePath = this.adjustedTickDelay(10);
            if (entity.distanceToSqr(owner) >= (TELEPORT_DISTANCE * TELEPORT_DISTANCE)) {
                teleportTo(owner);
            } else {
                entity.getNavigation().moveTo(owner, 1);
            }
        }
    }

    private void teleportTo(Entity owner) {
        BlockPos blockpos = owner.blockPosition();
        BlockPos teleportPos = NarakaUtils.randomBlockPos(entity.getRandom(), blockpos, 5);

        entity.moveTo(NarakaUtils.findAir(entity.level(), teleportPos, Direction.UP), 0, 0);
    }
}

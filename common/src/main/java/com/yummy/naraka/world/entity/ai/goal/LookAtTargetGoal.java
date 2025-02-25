package com.yummy.naraka.world.entity.ai.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

public class LookAtTargetGoal extends Goal {
    private final Mob mob;
    private @Nullable Entity target;

    public LookAtTargetGoal(Mob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        this.target = mob.getTarget();
        return target != null;
    }

    @Override
    public void tick() {
        if (target != null)
            mob.getLookControl().setLookAt(target);
    }
}

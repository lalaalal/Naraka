package com.yummy.naraka.world.entity.ai.goal;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

public class LookAtTargetGoal extends Goal {
    private final AbstractHerobrine herobrine;
    private @Nullable Entity target;

    public LookAtTargetGoal(AbstractHerobrine herobrine) {
        this.herobrine = herobrine;
    }

    @Override
    public boolean canUse() {
        this.target = herobrine.getTarget();
        return !herobrine.isStaggering() && target != null;
    }

    @Override
    public void tick() {
        if (target != null)
            herobrine.getLookControl().setLookAt(target);
    }
}

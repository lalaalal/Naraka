package com.yummy.naraka.world.entity.ai.goal;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class LookAtTargetGoal extends Goal {
    private final AbstractHerobrine herobrine;

    public LookAtTargetGoal(AbstractHerobrine herobrine) {
        this.herobrine = herobrine;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = herobrine.getTarget();
        return !herobrine.isStaggering() && !herobrine.isUsingSkill() && target != null;
    }

    @Override
    public void tick() {
        LivingEntity target = herobrine.getTarget();
        if (target != null)
            herobrine.getLookControl().setLookAt(target);
    }
}

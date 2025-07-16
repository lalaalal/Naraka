package com.yummy.naraka.world.entity.ai.goal;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class LookAtTargetGoal extends Goal {
    private final SkillUsingMob skillUsingMob;

    public LookAtTargetGoal(SkillUsingMob skillUsingMob) {
        this.skillUsingMob = skillUsingMob;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = skillUsingMob.getTarget();
        if (target == null)
            return false;
        return !skillUsingMob.isPlayingStaticAnimation() && !skillUsingMob.isUsingSkill();
    }

    @Override
    public void tick() {
        LivingEntity target = skillUsingMob.getTarget();
        if (target != null)
            skillUsingMob.getLookControl().setLookAt(target);
    }
}

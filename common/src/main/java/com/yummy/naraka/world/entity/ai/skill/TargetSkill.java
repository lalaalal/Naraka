package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class TargetSkill<T extends SkillUsingMob> extends Skill<T> {
    protected TargetSkill(ResourceLocation location, T mob, int duration, int cooldown, @Nullable Skill<?> linkedSkill) {
        super(location, mob, duration, cooldown, linkedSkill);
    }

    protected TargetSkill(ResourceLocation location, T mob, int duration, int cooldown) {
        super(location, mob, duration, cooldown);
    }

    @Override
    protected final void skillTick(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        tickAlways(level, target);
        if (target != null)
            tickWithTarget(level, target);
    }

    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {

    }

    /**
     * Called only when the target entity is not null.
     */
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {

    }

    protected void lookTarget(LivingEntity target) {
        mob.getLookControl().setLookAt(target);
    }

    protected final void rotateTowardTarget(LivingEntity target) {
        mob.lookAt(target, 180, 0);
    }

    protected final void moveToTarget(LivingEntity target, double speed) {
        if (mob.distanceToSqr(target) < 6) {
            mob.setDeltaMovement(Vec3.ZERO);
            return;
        }
        Vec3 deltaMovement = target.position().subtract(mob.position())
                .normalize()
                .scale(speed);
        if (mob.distanceToSqr(target) > 3)
            mob.setDeltaMovement(deltaMovement);
    }
}

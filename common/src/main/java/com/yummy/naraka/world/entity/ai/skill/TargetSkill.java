package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class TargetSkill<T extends SkillUsingMob> extends Skill<T> {
    protected TargetSkill(ResourceLocation location, int duration, int cooldown, T mob, @Nullable Skill<?> linkedSkill) {
        super(location, duration, cooldown, mob, linkedSkill);
    }

    protected TargetSkill(String name, int duration, int cooldown, T mob) {
        super(name, duration, cooldown, mob);
    }

    protected TargetSkill(ResourceLocation location, int duration, int cooldown, T mob) {
        super(location, duration, cooldown, mob);
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
     * Called only when target entity is not null.
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

package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

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

    protected final void moveToTarget(LivingEntity target, boolean stopOnGround, double speed) {
        moveToTarget(target, stopOnGround, deltaMovement -> deltaMovement.normalize().scale(speed));
    }

    protected void moveToTarget(LivingEntity target, boolean stopOnGround, UnaryOperator<Vec3> modifier) {
        if (mob.distanceToSqr(target) < 9 || (stopOnGround && mob.onGround())) {
            mob.setDeltaMovement(modifier.apply(Vec3.ZERO));
            return;
        }
        Vec3 deltaMovement = target.position()
                .subtract(mob.position());
        mob.setDeltaMovement(modifier.apply(deltaMovement));
    }

    protected void teleportToTarget(LivingEntity target, double distance) {
        Vec3 delta = target.getLookAngle().scale(distance);
        Vec3 position = target.position().add(delta);
        mob.teleportTo(position.x, mob.getY(), position.z);
        mob.level().playSound(null, mob, SoundEvents.PLAYER_TELEPORT, SoundSource.HOSTILE, 1, 1);
    }

    protected final boolean targetInRange(float distanceSquare) {
        LivingEntity target = mob.getTarget();
        return target != null && targetInRange(target, distanceSquare);
    }

    protected final boolean targetInRange(LivingEntity target, float distanceSquare) {
        return mob.distanceToSqr(target) <= distanceSquare;
    }

    protected final boolean targetOutOfRange(float distanceSquare) {
        LivingEntity target = mob.getTarget();
        return target != null && !targetInRange(distanceSquare);
    }

    protected final boolean targetOutOfRange(LivingEntity target, float distanceSquare) {
        return !targetInRange(target, distanceSquare);
    }

    protected final boolean targetInLookAngle(Vec3 target, float from, float to) {
        Vec3 delta = target.subtract(mob.position());
        double angle = NarakaUtils.wrapRadians(Math.atan2(delta.z, delta.horizontalDistance()) - Math.toRadians(mob.getYRot() + 90));
        return from <= angle && angle <= to;
    }

    protected final boolean targetInLookAngle(LivingEntity target, float from, float to) {
        return targetInLookAngle(target.position(), from, to);
    }
}

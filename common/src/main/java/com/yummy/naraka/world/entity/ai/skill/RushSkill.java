package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.StigmatizingEntity;
import com.yummy.naraka.world.entity.StunHelper;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RushSkill<T extends SkillUsingMob & StigmatizingEntity> extends AttackSkill<T> {
    public static final ResourceLocation LOCATION = createLocation("rush");

    private static final int START_RUNNING_TICK = 15;
    private static final int RUSH_TICK = 20;
    private static final int FINALE_TICK = 50;

    private Vec3 movement = Vec3.ZERO;
    private boolean hit = false;
    private boolean failed = false;

    private final DashSkill<?> dashSkill;

    public RushSkill(T mob, DashSkill<?> dashSkill) {
        super(LOCATION, 200, 200, mob);
        this.dashSkill = dashSkill;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetOutOfRange(15);
    }

    @Override
    public void prepare() {
        super.prepare();
        duration = 200;
        hit = false;
        failed = false;
        if (targetInRange(42)) {
            DashSkill.setupDashBack(dashSkill, this);
            mob.getSkillManager().setCurrentSkill(dashSkill);
        }
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        mob.getNavigation().stop();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(START_RUNNING_TICK, () -> lookTarget(target));
        runBefore(START_RUNNING_TICK, () -> rotateTowardTarget(target));
        runBefore(RUSH_TICK, () -> traceTarget(target));
        runAfter(duration - 40, () -> lookTarget(target));
        run(after(duration - 40) && failed, () -> rotateTowardTarget(target));
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAfter(START_RUNNING_TICK, this::moving);
        runAfter(RUSH_TICK, () -> calculateDeltaMovement(level, target));
        run(after(RUSH_TICK) && !hit && !failed, () -> hurtHitEntities(level, this::entityPredicate, 3));
        runAt(FINALE_TICK, this::failed);
        this.movement = movement.add(0, -0.098, 0);
    }

    private boolean entityPredicate(LivingEntity entity) {
        return AbstractHerobrine.isNotHerobrine(entity) && entity != mob.getTarget();
    }

    private void moving() {
        mob.setDeltaMovement(movement);
    }

    private void failed() {
        if (this.hit)
            return;
        this.duration = tickCount + 40;
        this.failed = true;
        mob.setAnimation(AnimationLocations.RUSH_FAILED);
    }

    private boolean isTargetInFront(@Nullable LivingEntity target) {
        if (target == null)
            return false;
        Vec3 delta = NarakaEntityUtils.getDirectionNormalVector(mob, target);
        return movement.dot(delta) > 0;
    }

    private void calculateDeltaMovement(ServerLevel level, @Nullable LivingEntity target) {
        boolean hitEntity = targetInRange(4) && isTargetInFront(target);
        Vec3 normalMovement = movement.normalize();
        if (this.failed) {
            this.movement = movement.scale(0.8);
        } else if (this.hit) {
            if (mob.onGround() && movement.y < 0)
                this.movement = movement.multiply(0, 1, 0);
        } else if (hitEntity || !mob.isFree(normalMovement.x, 0, normalMovement.z)) {
            this.duration = tickCount + 50;
            this.movement = movement.scale(-1)
                    .normalize()
                    .multiply(0.5, 0, 0.5)
                    .add(0, 1, 0);
            this.hit = true;
            this.failed = false;
            if (hitEntity)
                hurtHitEntity(level, target);
            level.playSound(mob, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 2, 1);
            level.sendParticles(ParticleTypes.SONIC_BOOM, mob.getX(), mob.getY() + 1, mob.getZ(), 1, 0, 0, 0, 1);
            mob.setAnimation(AnimationLocations.RUSH_SUCCEED);
        }
    }

    private void traceTarget(LivingEntity target) {
        if (before(START_RUNNING_TICK) || isTargetInFront(target)) {
            this.movement = NarakaEntityUtils.getDirectionNormalVector(mob, target)
                    .multiply(1, 0, 1);
        }
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (NarakaEntityUtils.disableAndHurtShield(target, 20 * 5, 15))
            return;
        StunHelper.stunEntity(target, 100);
        mob.stigmatizeEntity(level, target);
        super.hurtHitEntity(level, target);
        Vec3 delta = NarakaEntityUtils.getDirectionNormalVector(mob, target);
        target.knockback(5, -delta.x, -delta.z);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.08f;
    }
}

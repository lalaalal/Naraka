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
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public class RushSkill<T extends SkillUsingMob & StigmatizingEntity> extends AttackSkill<T> {
    public static final ResourceLocation LOCATION = createLocation("rush");

    private static final int START_RUNNING_TICK = 20;
    private static final int RUSH_TICK = 25;
    private static final int FINALE_TICK = 50;

    private Vec3 deltaMovement = Vec3.ZERO;
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
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        mob.getNavigation().stop();
        if (targetInRange(25)) {
            DashSkill.setupDashBack(dashSkill, this);
            mob.getSkillManager().setCurrentSkill(dashSkill);
        }
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(START_RUNNING_TICK, () -> lookTarget(target));
        runBefore(START_RUNNING_TICK, () -> rotateTowardTarget(target));
        runBefore(RUSH_TICK, () -> traceTarget(target));
        runAfter(duration - 40, () -> lookTarget(target));
        runAfter(duration - 40, () -> rotateTowardTarget(target));
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAfter(START_RUNNING_TICK - 5, this::moving);
        runAt(FINALE_TICK, this::failed);
        runAfter(RUSH_TICK, () -> calculateDeltaMovement(level));
        this.deltaMovement = deltaMovement.add(0, -0.098, 0);
    }

    private void moving() {
        mob.setDeltaMovement(deltaMovement);
    }

    private void failed() {
        if (this.hit)
            return;
        this.duration = tickCount + 40;
        this.failed = true;
        mob.setAnimation(AnimationLocations.RUSH_FAILED);
    }

    private void calculateDeltaMovement(ServerLevel level) {
        Collection<LivingEntity> entities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), mob, mob.getBoundingBox().inflate(1));
        Optional<LivingEntity> hitEntity = entities.stream()
                .filter(AbstractHerobrine::isNotHerobrine)
                .findAny();
        Vec3 normalMovement = deltaMovement.normalize();
        if (this.failed) {
            this.deltaMovement = deltaMovement.scale(0.8);
        } else if (this.hit) {
            if (mob.onGround() && deltaMovement.y < 0)
                this.deltaMovement = deltaMovement.multiply(0, 1, 0);
        } else if (hitEntity.isPresent() || !mob.isFree(normalMovement.x, 0, normalMovement.z)) {
            this.duration = tickCount + 50;
            this.deltaMovement = deltaMovement.scale(-1)
                    .normalize()
                    .multiply(0.5, 0, 0.5)
                    .add(0, 1, 0);
            this.hit = true;
            this.failed = false;
            hitEntity.ifPresent(entity -> hurtHitEntity(level, entity));
            level.playSound(mob, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 2, 1);
            level.sendParticles(ParticleTypes.SONIC_BOOM, mob.getX(), mob.getY() + 1, mob.getZ(), 1, 0, 0, 0, 1);
            mob.setAnimation(AnimationLocations.RUSH_SUCCEED);
        }
    }

    private void traceTarget(LivingEntity target) {
        this.deltaMovement = NarakaEntityUtils.getDirectionNormalVector(mob, target)
                .multiply(1, 0, 1);
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (NarakaEntityUtils.disableAndHurtShield(target, 20 * 5, 15))
            return;
        StunHelper.stunEntity(target, 100);
        mob.stigmatizeEntity(level, target);
        super.hurtHitEntity(level, target);
        Vec3 view = mob.getLookAngle();
        target.knockback(5, -view.x, -view.z);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.08f;
    }
}

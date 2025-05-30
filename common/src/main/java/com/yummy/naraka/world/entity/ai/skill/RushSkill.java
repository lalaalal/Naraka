package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.StigmatizingEntity;
import com.yummy.naraka.world.entity.StunHelper;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RushSkill<T extends SkillUsingMob & StigmatizingEntity> extends AttackSkill<T> {
    public static final ResourceLocation LOCATION = createLocation("rush");

    private static final int START_RUNNING_TICK = 20;
    private static final int RUSH_TICK = 25;
    private static final int FINALE_TICK = 50;

    private Vec3 deltaMovement = Vec3.ZERO;
    private boolean hit = false;
    private boolean failed = false;

    private final List<Entity> blockedEntities = new ArrayList<>();
    private final DashSkill<?> dashSkill;

    public RushSkill(T mob, DashSkill<?> dashSkill) {
        super(LOCATION, 200, 200, mob);
        this.dashSkill = dashSkill;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    public void prepare() {
        super.prepare();
        duration = 200;
        blockedEntities.clear();
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
        lookTarget(target);
        runBefore(RUSH_TICK, () -> rotateTowardTarget(target));

        runBefore(RUSH_TICK, () -> calculateDeltaMovement(level, target, true, 1));
        runAfter(RUSH_TICK, () -> calculateDeltaMovement(level, target, false, 3));

        runBetween(RUSH_TICK, FINALE_TICK, () -> hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 0.5));
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAfter(START_RUNNING_TICK - 5, () -> moving(level));
        runAt(FINALE_TICK, this::failed);
    }

    private void moving(ServerLevel level) {
        NarakaEntityUtils.updatePositionForUpStep(level, mob, deltaMovement, 0.5);
        mob.setDeltaMovement(deltaMovement);
    }

    private void failed() {
        if (this.hit)
            return;
        this.duration = tickCount + 40;
        this.failed = true;
        mob.setAnimation(AnimationLocations.RUSH_FAILED);
    }

    private void calculateDeltaMovement(ServerLevel level, LivingEntity target, boolean trace, double scale) {
        Collection<LivingEntity> entities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), mob, mob.getBoundingBox().inflate(0.5));
        Optional<LivingEntity> hitEntity = entities.stream()
                .filter(AbstractHerobrine::isNotHerobrine)
                .findAny();
        Vec3 view = mob.getLookAngle();
        BlockPos toward = NarakaUtils.pos(mob.position().add(view.multiply(1, 0, 1).normalize()));
        if (this.failed) {
            this.deltaMovement = deltaMovement.scale(0.8)
                    .add(0, -0.098, 0);
        } else if (this.hit) {
            if (mob.onGround() && deltaMovement.y < 0)
                this.deltaMovement = deltaMovement.multiply(0, 1, 0);
            this.deltaMovement = deltaMovement.add(0, -0.098, 0);
        } else if (hitEntity.isPresent() || isWall(level, toward)) {
            this.duration = tickCount + 50;
            this.deltaMovement = deltaMovement.yRot(Mth.PI)
                    .normalize()
                    .multiply(0.5, 0, 0.5)
                    .add(0, 1, 0);
            this.hit = true;
            this.failed = false;
            hitEntity.ifPresent(entity -> hurtHitEntity(level, entity));
            level.playSound(mob, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 2, 1);
            level.sendParticles(ParticleTypes.SONIC_BOOM, mob.getX(), mob.getY() + 1, mob.getZ(), 1, 0, 0, 0, 1);
            mob.setAnimation(AnimationLocations.RUSH_SUCCEED);
        } else if (trace) {
            this.deltaMovement = NarakaEntityUtils.getDirectionNormalVector(mob, target)
                    .multiply(1, 0, 1)
                    .add(0, -0.098, 0)
                    .scale(scale);
        }
    }

    private boolean isWall(Level level, BlockPos pos) {
        if (tickCount < RUSH_TICK)
            return false;
        return level.getBlockState(pos).canOcclude() || level.getBlockState(pos.above()).canOcclude();
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (NarakaEntityUtils.disableAndHurtShield(target, 20 * 5, 15)) {
            blockedEntities.add(target);
            return;
        }
        if (!blockedEntities.contains(target) && target.invulnerableTime < 10) {
            StunHelper.stunEntity(target, 100);
            mob.stigmatizeEntity(level, target);
            super.hurtHitEntity(level, target);
            Vec3 view = mob.getLookAngle();
            target.knockback(5, -view.x, -view.z);
        }
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.08f;
    }
}

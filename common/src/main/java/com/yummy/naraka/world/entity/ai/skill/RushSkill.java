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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RushSkill<T extends SkillUsingMob & StigmatizingEntity> extends AttackSkill<T> {
    public static final ResourceLocation LOCATION = createLocation("rush");

    private static final int START_RUNNING_TICK = 15;
    private static final int RUSH_TICK = 18;
    private static final int FINALE_TICK = 30;

    private Vec3 movement = Vec3.ZERO;
    private boolean hit = false;
    private boolean failed = false;

    public RushSkill(T mob) {
        super(LOCATION, 200, 200, 100, 15, mob);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        if (target == null || target.getY() > mob.getY())
            return false;

        return targetOutOfRange(target, 36) && noObstacle(target);
    }

    private boolean noObstacle(LivingEntity target) {
        Level level = target.level();
        Vec3 delta = target.position().subtract(mob.position());
        Vec3 normalDelta = delta.normalize();
        int blocks = Mth.ceil(delta.length());
        Vec3 current = mob.position();
        for (int i = 0; i < blocks; i++) {
            BlockPos blockPos = BlockPos.containing(current);
            if (level.getBlockState(blockPos).canOcclude() || level.getBlockState(blockPos.above()).canOcclude())
                return false;
            current = current.add(normalDelta);
        }
        return true;
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
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(START_RUNNING_TICK, () -> lookTarget(target));
        runBefore(START_RUNNING_TICK, () -> rotateTowardTarget(target));
        runBefore(RUSH_TICK, () -> traceTarget(target, 1));
        runAt(RUSH_TICK, () -> traceTarget(target, 1.5f));
        runAfter(duration - 40, () -> lookTarget(target));
        run(after(duration - 40) && failed, () -> rotateTowardTarget(target));
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAfter(START_RUNNING_TICK, this::moving);
        runAfter(RUSH_TICK, () -> calculateDeltaMovement(level, target));
        run(after(RUSH_TICK) && !hit && !failed, () -> hurtEntities(level, this::entityPredicate, 3));
        runAt(FINALE_TICK, this::failed);
        this.movement = movement.add(0, -0.098, 0);

        run(between(RUSH_TICK, FINALE_TICK) && !hit && !failed, () -> blowBlocks(level, Mth.PI * 0.67f, 1, 1));
        run(between(RUSH_TICK, FINALE_TICK) && !hit && !failed, () -> blowBlocks(level, Mth.PI * 0.87f, 1.5, 1.2));
    }

    private void blowBlocks(ServerLevel level, float rotation, double distance, double power) {
        Vec3 lookAngle = mob.getLookAngle();
        blowBlock(level, lookAngle.yRot(rotation), distance, power);
        blowBlock(level, lookAngle.yRot(-rotation), distance, power);
    }

    private void blowBlock(ServerLevel level, Vec3 current, double distance, double power) {
        BlockPos pos = BlockPos.containing(mob.position().add(current.normalize().scale(distance)));
        BlockPos floor = NarakaUtils.findFloor(level, pos);
        BlockState state = level.getBlockState(floor);
        Vec3 movement = current.add(0, 0.3, 0).scale(power);
        NarakaEntityUtils.createFloatingBlock(level, floor.above(), state, movement);
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
                    .multiply(0.6, 0, 0.6)
                    .add(0, 1.1, 0);
            this.hit = true;
            this.failed = false;
            if (hitEntity)
                hurtEntity(level, target);
            level.playSound(mob, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 2, 1);
            level.sendParticles(ParticleTypes.SONIC_BOOM, mob.getX(), mob.getY() + 1, mob.getZ(), 1, 0, 0, 0, 1);
            mob.setAnimation(AnimationLocations.RUSH_SUCCEED);
        }
    }

    private void traceTarget(LivingEntity target, float power) {
        this.movement = NarakaEntityUtils.getDirectionNormalVector(mob, target)
                .multiply(1, 0, 1)
                .scale(power);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        StunHelper.stunEntity(target, 100);
        mob.stigmatizeEntity(level, target);
        Vec3 delta = NarakaEntityUtils.getDirectionNormalVector(mob, target);
        target.knockback(5, -delta.x, -delta.z);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.08f;
    }
}

package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.block.UnstableBlock;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.StigmatizingEntity;
import com.yummy.naraka.world.entity.StunHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RushSkill<T extends SkillUsingMob & StigmatizingEntity> extends Skill<T> {
    public static final String NAME = "rush";

    private static final int START_RUNNING_TICK = 25;
    private static final int STOP_RUNNING_TICK = 35;
    private static final int RUSH_TICK = 40;
    private static final int FINALE_TICK = 55;

    private Vec3 delta = Vec3.ZERO;

    private final List<Entity> blockedEntities = new ArrayList<>();
    private Predicate<LivingEntity> targetPredicate = livingEntity -> true;

    public RushSkill(T mob) {
        super(NAME, 85, 160, mob);
    }

    public RushSkill(T mob, Predicate<LivingEntity> targetPredicate) {
        super(NAME, 85, 160, mob);
        this.targetPredicate = targetPredicate;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null;
    }

    @Override
    public void prepare() {
        super.prepare();
        blockedEntities.clear();
    }

    @Override
    protected void skillTick() {
        LivingEntity target = mob.getTarget();
        if (target == null)
            return;

        mob.getNavigation().stop();
        updateDeltaMovement(target, START_RUNNING_TICK, STOP_RUNNING_TICK, 0.6, true, true);
        updateDeltaMovement(target, STOP_RUNNING_TICK, RUSH_TICK, 0, true, false);
        updateDeltaMovement(target, RUSH_TICK, FINALE_TICK, 1.15, false, false);
        if (RUSH_TICK <= tickCount && tickCount <= FINALE_TICK) {
            hurtHitEntities();
//            updateBlocks(7 - (tickCount - RUSH_TICK));
        }
        if (tickCount >= FINALE_TICK)
            delta = delta.scale(0.5f);
    }

    private void calculateDeltaMovement(LivingEntity target, boolean ignoreDeltaY) {
        int factor = ignoreDeltaY ? 0 : 1;
        this.delta = NarakaEntityUtils.getDirectionNormalVector(mob, target).multiply(1, factor, 1);
        mob.lookAt(target, 360, 0);
    }

    private void updateDeltaMovement(LivingEntity target, int startTick, int endTick, double scale, boolean updateDeltaMovement, boolean ignoreDeltaY) {
        if (updateDeltaMovement && tickCount == startTick)
            calculateDeltaMovement(target, ignoreDeltaY);
        if (startTick <= tickCount && tickCount < endTick) {
            NarakaEntityUtils.updatePositionForUpStep(level(), mob, delta, 0.5);
            mob.setDeltaMovement(delta.scale(scale));
        }
    }

    private void updateBlocks(int power) {
        Level level = mob.level();
        RandomSource random = mob.getRandom();
        for (int i = 0; i < power; i++) {
            double distance = random.nextDouble() * power;
            double angle = random.nextDouble() * Math.PI * 2;

            int x = mob.blockPosition().getX() + (int) Math.round(Math.cos(angle) * distance);
            int z = mob.blockPosition().getZ() + (int) Math.round(Math.sin(angle) * distance);

            BlockPos pos = new BlockPos(x, mob.getBlockY() - 1, z);
            UnstableBlock.makeUnstable(level, pos);
        }
    }

    private void hurtHitEntities() {
        mob.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), mob, mob.getBoundingBox().inflate(0.5))
                .stream()
                .filter(targetPredicate)
                .forEach(this::hurtHitTarget);
    }

    private void hurtHitTarget(LivingEntity target) {
        if (NarakaEntityUtils.disableAndHurtShield(target, 20 * 8, 30)) {
            blockedEntities.add(target);
            return;
        }
        DamageSource source = mob.getDefaultDamageSource();
        float damage = mob.getAttackDamage() + target.getMaxHealth() * 0.08f;
        if (!blockedEntities.contains(target) && target.invulnerableTime < 10) {
            StunHelper.stunEntity(target, 100);
            mob.stigmatizeEntity(target);
        }
        target.hurt(source, damage);
        target.knockback(5, mob.getX() - target.getX(), mob.getZ() - target.getZ());
    }
}

package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.StunHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public class RushSkill extends Skill {
    public static final String NAME = "rush";

    private static final int START_RUNNING_TICK = 25;
    private static final int STOP_RUNNING_TICK = 35;
    private static final int RUSH_TICK = 40;
    private static final int FINALE_TICK = 55;

    private Vec3 delta = Vec3.ZERO;

    public RushSkill(SkillUsingMob mob) {
        super(NAME, 85, 160, mob);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && mob.distanceToSqr(target) > 5 * 5;
    }

    @Override
    protected void skillTick() {
        LivingEntity target = mob.getTarget();
        if (target == null)
            return;

        mob.getNavigation().stop();
        updateDeltaMovement(target, START_RUNNING_TICK, STOP_RUNNING_TICK, 1);
        updateDeltaMovement(target, STOP_RUNNING_TICK, RUSH_TICK, 0);
        updateDeltaMovement(target, RUSH_TICK, FINALE_TICK, 1.15);
        if (RUSH_TICK <= tickCount && tickCount <= FINALE_TICK) {
            hurtHitEntities();
            updateBlocks(7 - (tickCount - RUSH_TICK));
        }
        if (tickCount >= FINALE_TICK)
            delta = delta.scale(0.5f);
    }

    private void calculateDeltaMovement(LivingEntity target) {
        this.delta = NarakaEntityUtils.getDirectionNormalVector(mob, target);
        mob.lookAt(target, 360, 0);
    }

    private void updateDeltaMovement(LivingEntity target, int startTick, int endTick, double scale) {
        if (tickCount == startTick)
            calculateDeltaMovement(target);
        if (startTick <= tickCount && tickCount < endTick)
            mob.setDeltaMovement(delta.scale(scale));
    }

    private void updateBlocks(int power) {
        Level level = mob.level();
        RandomSource random = mob.getRandom();
        for (int i = 0; i < power; i++) {
            double distance = random.nextDouble() * power;
            double angle = random.nextDouble() * Math.PI * 2;

            int x = mob.blockPosition().getX() + (int) Math.round(Math.cos(angle) * distance);
            int z = mob.blockPosition().getZ() + (int) Math.round(Math.sin(angle) * distance);

            level.setBlock(new BlockPos(x, mob.getBlockY() - 1, z), NarakaBlocks.UNSTABLE_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        }
    }

    private void hurtHitEntities() {
        mob.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), mob, mob.getBoundingBox().inflate(1))
                .forEach(this::hurtHitTarget);
    }

    private void hurtHitTarget(LivingEntity target) {
        if (target.isBlocking() && target instanceof Player player) {
            player.getCooldowns().addCooldown(Items.SHIELD, 20 * 8);
            return;
        }

        DamageSource source = NarakaDamageSources.fixed(mob);
        target.hurt(source, 3);
        target.knockback(5, mob.getX() - target.getX(), mob.getZ() - target.getZ());
        StunHelper.stunEntity(target, 20 * 5);
    }
}

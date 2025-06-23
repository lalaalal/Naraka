package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class UppercutSkill extends ComboSkill<AbstractHerobrine> {
    public static final String NAME = "uppercut";

    public UppercutSkill(@Nullable ComboSkill<AbstractHerobrine> comboSkill, AbstractHerobrine mob) {
        super(createLocation(NAME), 35, 0, 0.1f, comboSkill, 15, mob);
        this.shieldCooldown = 60;
        this.shieldDamage = 15;
    }

    @Override
    protected float getBonusChance() {
        if (targetInRange(9))
            return 0.6f;
        return super.getBonusChance();
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        mob.getNavigation().stop();
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(10, this::jump);
        runAt(12, this::floating);
    }

    private void jump() {
        mob.addDeltaMovement(new Vec3(0, 0.5, 0));
    }

    private void floating() {
        if (hasLinkedSkill()) {
            mob.setNoGravity(true);
            mob.setDeltaMovement(0, 0, 0);
        }
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(10, () -> moveToTarget(target));
        lookTarget(target);
        rotateTowardTarget(target);
        runAt(10, () -> this.hurtEntity(level, target));
    }

    @Override
    protected boolean hurtEntity(ServerLevel level, LivingEntity target) {
        if (!targetInRange(target, 6))
            return true;
        return super.hurtEntity(level, target);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        Vec3 movement = new Vec3(0, 0.4, 0);
        target.addDeltaMovement(movement);
        if (target instanceof ServerPlayer serverPlayer)
            NarakaEntityUtils.sendPlayerMovement(serverPlayer, movement.scale(4));
        mob.stigmatizeEntity(level, target);
        level.playSound(mob, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.03f;
    }

    private void moveToTarget(LivingEntity target) {
        if (tickCount == 10)
            mob.setDeltaMovement(0, mob.getDeltaMovement().y, 0);
        if (tickCount >= 10)
            return;

        Vec3 deltaMovement = target.position().subtract(mob.position())
                .normalize()
                .scale(0.4);
        if (mob.distanceToSqr(target) > 3)
            mob.setDeltaMovement(deltaMovement);
    }

    @Override
    public void interrupt() {
        mob.setNoGravity(false);
    }
}

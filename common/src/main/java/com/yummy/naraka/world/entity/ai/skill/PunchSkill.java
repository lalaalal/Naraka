package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.StunHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class PunchSkill extends ComboSkill<AbstractHerobrine> {
    public static final ResourceLocation LOCATION = createLocation("punch");
    public static final int DEFAULT_COOLDOWN = 200;

    private final boolean stunTarget;

    public PunchSkill(ComboSkill<AbstractHerobrine> comboSkill, AbstractHerobrine mob, boolean withStun) {
        super(LOCATION, 35, DEFAULT_COOLDOWN, 0.8f, comboSkill, 15, mob);
        this.stunTarget = withStun;
    }

    public PunchSkill(ComboSkill<AbstractHerobrine> comboSkill, AbstractHerobrine mob, int cooldown, boolean withStun) {
        super(LOCATION, 35, cooldown, 0.8f, comboSkill, 15, mob);
        this.stunTarget = withStun;
    }

    @Override
    public boolean canUse() {
        return targetInRange(4);
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        mob.getNavigation().stop();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(10, this::hurtHitEntity, level, target);
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (NarakaEntityUtils.disableAndHurtShield(target, 100, 25) || !targetInRange(target, 4))
            return;
        super.hurtHitEntity(level, target);
        if (stunTarget)
            StunHelper.stunEntity(target, 20, true);
        mob.stigmatizeEntity(level, target);
        level.playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.03f;
    }

    private void moveToTarget(LivingEntity target) {
        if (tickCount < 7)
            mob.getLookControl().setLookAt(target);
        if (tickCount == 10)
            mob.setDeltaMovement(Vec3.ZERO);
        if (tickCount >= 10)
            return;

        Vec3 deltaMovement = target.position().subtract(mob.position())
                .normalize()
                .scale(0.7);
        if (mob.distanceToSqr(target) > 3)
            mob.setDeltaMovement(deltaMovement);
    }
}

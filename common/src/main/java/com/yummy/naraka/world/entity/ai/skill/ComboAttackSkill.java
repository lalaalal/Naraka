package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.StigmatizingEntity;
import com.yummy.naraka.world.entity.StunHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ComboAttackSkill<T extends SkillUsingMob & StigmatizingEntity> extends Skill<T> {
    public static final ResourceLocation LOCATION = createLocation("combo_attack");
    public static final int DEFAULT_COOLDOWN = 20;

    private int linkedCount = 1;
    private int maxLinkCount = 5;
    private boolean stunTarget = true;
    private boolean traceTarget = true;

    public ComboAttackSkill(T mob) {
        super(LOCATION, 20, DEFAULT_COOLDOWN, mob);
    }

    public void setMaxLinkCount(int maxLinkCount) {
        this.maxLinkCount = maxLinkCount;
    }

    public int getLinkedCount() {
        return linkedCount;
    }

    public void setStunTarget(boolean stunTarget) {
        this.stunTarget = stunTarget;
    }

    public void setTraceTarget(boolean traceTarget) {
        this.traceTarget = traceTarget;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && mob.distanceToSqr(target) <= 4;
    }

    @Override
    public boolean readyToUse() {
        return super.readyToUse();
    }

    @Override
    public void prepare() {
        if (getLinkedCount() == 1)
            cooldown = DEFAULT_COOLDOWN;
        else
            cooldown += DEFAULT_COOLDOWN;
        super.prepare();
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        if (linkedCount < maxLinkCount && level.random.nextFloat() < 0.8f) {
            setLinkedSkill(this);
            linkedCount += 1;
        } else {
            setLinkedSkill(null);
            linkedCount = 1;
        }
    }

    @Override
    protected void skillTick(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        if (target == null)
            return;

        if (traceTarget && linkedCount > 1)
            moveToTarget(target);

        if (tickCount == 8)
            hurtTarget(level, target);
    }

    private void moveToTarget(LivingEntity target) {
        if (tickCount == 5)
            mob.setDeltaMovement(Vec3.ZERO);
        if (tickCount >= 5)
            return;
        mob.lookAt(target, 180, 0);
        Vec3 deltaMovement = target.position().subtract(mob.position())
                .normalize()
                .scale(0.7);
        if (mob.distanceToSqr(target) > 3)
            mob.setDeltaMovement(deltaMovement);
    }

    private void hurtTarget(ServerLevel level, LivingEntity target) {
        DamageSource damageSource = mob.getDefaultDamageSource();
        float damage = mob.getAttackDamage() + target.getMaxHealth() * 0.03f;
        int shieldCooldown = linkedCount == 1 ? 0 : 100;
        if (NarakaEntityUtils.disableAndHurtShield(target, shieldCooldown, 25) || !canUse())
            return;

        if (linkedCount == 1 && stunTarget)
            StunHelper.stunEntity(target, 40);
        target.hurtServer(level, damageSource, damage);
        target.knockback(2f, mob.getX() - target.getX(), mob.getZ() - target.getZ());
        mob.stigmatizeEntity(target);
    }
}

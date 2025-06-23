package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.StunHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class PunchSkill<T extends AbstractHerobrine> extends ComboSkill<T> {
    public static final ResourceLocation LOCATION = createLocation("punch");

    private boolean linked = false;

    public PunchSkill(@Nullable ComboSkill<?> comboSkill, T mob, int cooldown, boolean disableShield) {
        super(LOCATION, 22, cooldown, 0.8f, comboSkill, 11, mob);
        if (disableShield) {
            this.shieldCooldown = 60;
            this.shieldDamage = 15;
        }
    }

    public void setLinkedFromPrevious(boolean linked) {
        this.linked = linked;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetInRange(4);
    }

    @Override
    protected float getBonusChance() {
        if (linked)
            return -0.4f;
        return super.getBonusChance();
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        mob.getNavigation().stop();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(10, () -> this.hurtEntity(level, target));
        lookTarget(target);
        rotateTowardTarget(target);
    }

    @Override
    protected boolean hurtEntity(ServerLevel level, LivingEntity target) {
        if (targetOutOfRange(target, 4))
            return false;

        return super.hurtEntity(level, target);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        if (canDisableShield())
            StunHelper.stunEntity(target, 100, true);
        mob.stigmatizeEntity(level, target);
        level.playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        linked = false;
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.03f;
    }
}

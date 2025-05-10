package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.StunHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SuperHitSkill extends ComboSkill<AbstractHerobrine> {
    public static final String NAME = "super_hit";

    public SuperHitSkill(ComboSkill<AbstractHerobrine> comboSkill, AbstractHerobrine mob) {
        super(createLocation(NAME), 20, 0, 1, comboSkill, 20, mob);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(8, this::superHit);
        if (tickCount > 10)
            hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 1.5);
        if (tickCount > 10 && mob.onGround()) {
            duration = 0;
            level.playSound(mob, mob.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1);
        }
    }

    private void superHit() {
        mob.setNoGravity(false);
        Vec3 lookVector = mob.getLookAngle().scale(1.2);
        mob.setDeltaMovement(lookVector.x, -1.2, lookVector.z);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        mob.lookAt(target, 180, 0);
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (NarakaEntityUtils.disableAndHurtShield(target, 60, 15))
            return;
        super.hurtHitEntity(level, target);
        StunHelper.stunEntity(target, 20, true);
        mob.stigmatizeEntity(level, target);
        level.playSound(mob, mob.blockPosition(), SoundEvents.STONE_BREAK, SoundSource.HOSTILE, 1, 1);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.03f;
    }
}

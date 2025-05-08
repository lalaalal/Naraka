package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class SpinningSkill extends ComboSkill<AbstractHerobrine> {
    public static final String NAME = "spinning";

    public SpinningSkill(ComboSkill<AbstractHerobrine> comboSkill, AbstractHerobrine mob) {
        super(createLocation(NAME), 30, 0, 1, comboSkill, 30, mob);
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        if (target != null)
            mob.getLookControl().setLookAt(target);
        mob.setNoGravity(false);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(10, this::spinAttack, level);
    }

    private void spinAttack(ServerLevel level) {
        hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 4);
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        super.hurtHitEntity(level, target);
        mob.stigmatizeEntity(level, target);
        level.playSound(mob, mob.blockPosition(), SoundEvents.ANVIL_DESTROY, SoundSource.HOSTILE, 1, 1);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.03f;
    }

    @Override
    public void interrupt() {
        mob.setNoGravity(false);
    }
}

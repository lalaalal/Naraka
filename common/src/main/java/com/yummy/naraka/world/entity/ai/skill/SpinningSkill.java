package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.StunHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class SpinningSkill extends ComboSkill<AbstractHerobrine> {
    public static final String NAME = "spinning";

    public SpinningSkill(ComboSkill<AbstractHerobrine> comboSkill, AbstractHerobrine mob) {
        super(createLocation(NAME), 20, 0, 1, comboSkill, 20, mob);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(10, () -> this.spinAttack(level));
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(duration - 1, () -> rotateTowardTarget(target));
    }

    private void spinAttack(ServerLevel level) {
        hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 4);
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (NarakaEntityUtils.disableAndHurtShield(target, 60, 15))
            return;
        super.hurtHitEntity(level, target);
        mob.stigmatizeEntity(level, target);
        StunHelper.stunEntity(target, 20, true);
        level.playSound(mob, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
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

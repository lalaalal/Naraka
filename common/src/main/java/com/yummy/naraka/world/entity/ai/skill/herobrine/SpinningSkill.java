package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.ai.skill.ComboSkill;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class SpinningSkill extends ComboSkill<AbstractHerobrine> {
    public static final Identifier LOCATION = createLocation("herobrine.spinning");

    public SpinningSkill(AbstractHerobrine mob, Skill<?> comboSkill) {
        super(LOCATION, mob, 20, 0, 1, 20, comboSkill);
        this.shieldCooldown = 60;
        this.shieldDamage = 15;
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
        level.sendParticles(ParticleTypes.SWEEP_ATTACK, mob.getX(), mob.getY(), mob.getZ(), 1, 0, 0, 0, 1);
        hurtEntities(level, AbstractHerobrine::isNotHerobrine, 4);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
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

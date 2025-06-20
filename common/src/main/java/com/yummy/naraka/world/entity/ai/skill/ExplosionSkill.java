package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ExplosionSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("explosion");

    public ExplosionSkill(Herobrine mob) {
        super(LOCATION, 130, 200, mob);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(0, () -> mob.setDeltaMovement(0, 0.2, 0));
        runBetween(0, 18, () -> mob.setDeltaMovement(mob.getDeltaMovement().scale(0.8)));
        runAt(19, () -> mob.setDeltaMovement(0, -8, 0));

        runAt(58, () -> mob.setDisplayPickaxe(false));
        runAt(60, () -> mob.setDeltaMovement(0, 0.4, 0));
        runAt(60, () -> level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE));
        runBetween(60, 70, () -> sendParticles(level));
        runAt(62, () -> mob.setDeltaMovement(Vec3.ZERO));

        runAt(95, () -> mob.setDisplayPickaxe(true));
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAfter(95, () -> lookTarget(target));
    }

    private void sendParticles(ServerLevel level) {
        level.sendParticles(ParticleTypes.FLASH, mob.getX(), mob.getEyeY(), mob.getZ(), 20, 1, 2, 1, 1);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return 10;
    }
}

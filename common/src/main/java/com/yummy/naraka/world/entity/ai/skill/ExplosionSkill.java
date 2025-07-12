package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.MagicCircle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ExplosionSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final_herobrine.explosion");

    @Nullable
    private MagicCircle magicCircle;

    public ExplosionSkill(Herobrine mob) {
        super(LOCATION, mob, 130, 300);
    }

    @Override
    public void prepare() {
        super.prepare();
        magicCircle = null;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetOutOfRange(16);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(0, () -> mob.setDeltaMovement(0, 0.2, 0));
        runBetween(0, 18, () -> reduceSpeed(0.8));
        runAt(19, () -> spawnMagicCircle(level));
        runBetween(20, 41, () -> scaleMagicCircle(0, 30, 20, 40));

        runAt(60, () -> mob.setDeltaMovement(0, 0.4, 0));
        runAt(60, () -> level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE));
        runBetween(60, 70, () -> level.sendParticles(ParticleTypes.FLASH, mob.getX(), mob.getEyeY(), mob.getZ(), 20, 1, 2, 1, 1));
        runBetween(85, 90, () -> scaleMagicCircle(30, 0, 85, 89));

        runAt(62, () -> mob.setDeltaMovement(Vec3.ZERO));
        runAt(107, () -> hurtEntities(level, this::checkTarget, 5));
    }

    private boolean checkTarget(LivingEntity target) {
        return targetInLookAngle(target, -Mth.HALF_PI, Mth.HALF_PI) && AbstractHerobrine.isNotHerobrine(target);
    }

    private void scaleMagicCircle(float from, float to, int startTick, int endTick) {
        if (magicCircle == null)
            return;
        int duration = endTick - startTick;
        int scaleTick = tickCount - startTick;
        float delta = scaleTick / (float) duration;
        float scale = NarakaUtils.interpolate(delta, from, to, NarakaUtils::fastStepOut);
        magicCircle.setScale(scale);
    }

    private void spawnMagicCircle(ServerLevel level) {
        mob.setDeltaMovement(0, -8, 0);
        BlockPos floor = NarakaUtils.findFloor(level, mob.blockPosition());
        magicCircle = new MagicCircle(level, mob, 80, 0);
        magicCircle.setPos(mob.getX(), floor.above().getY() + 0.1, mob.getZ());
        level.addFreshEntity(magicCircle);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(0, () -> NarakaSkillUtils.sendParticleFront(level, mob, target, NarakaParticleTypes.TELEPORT.get()));
        runAt(3, () -> teleportToTarget(target, 1));
        runBetween(0, 5, () -> rotateTowardTarget(target));
        runAfter(95, () -> lookTarget(target));
        runAfter(110, () -> rotateTowardTarget(target));
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return 10;
    }

    @Override
    protected void hurtEntities(ServerLevel level, Predicate<LivingEntity> predicate, double size) {
        super.hurtEntities(level, predicate, size);
        NarakaSkillUtils.sendCircleParticle(level, mob.position(), NarakaParticleTypes.GOLDEN_FLAME.get(), size);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
        level.playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }
}

package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class SuperHitSkill extends ComboSkill<AbstractHerobrine> {
    public static final String NAME = "super_hit";
    private int onGroundTick = 0;
    private final Set<LivingEntity> hitEntities = new HashSet<>();

    public SuperHitSkill(ComboSkill<AbstractHerobrine> comboSkill, AbstractHerobrine mob) {
        super(createLocation(NAME), 40, 0, 1, comboSkill, 40, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        onGroundTick = 0;
        hitEntities.clear();
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(5, this::superHit);
        runAfter(5, () -> hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 6));
        runAfter(5, () -> this.stopOnGround(level));
    }

    private void superHit() {
        mob.setNoGravity(false);
        Vec3 lookVector = mob.getLookAngle().scale(0.5f);
        mob.setDeltaMovement(lookVector.x, -1.2, lookVector.z);
    }

    private void stopOnGround(ServerLevel level) {
        if (onGroundTick > 1) {
            tickCount = duration;
            level.playSound(mob, mob.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1);
            mob.setDeltaMovement(mob.getDeltaMovement().multiply(0, 1, 0));
        }
        if (mob.onGround()) {
            level.sendParticles(ParticleTypes.FIREWORK, mob.getX(), mob.getY(), mob.getZ(), 10, 0.5, 1, 0.5, 0.3);
            NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 5 + onGroundTick);
            onGroundTick += 1;
        }
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(5, () -> rotateTowardTarget(target));
    }

    @Override
    protected boolean hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (NarakaEntityUtils.disableAndHurtShield(target, 60, 15) || hitEntities.contains(target))
            return true;
        hitEntities.add(target);
        mob.stigmatizeEntity(level, target);
        level.playSound(mob, mob.blockPosition(), SoundEvents.STONE_BREAK, SoundSource.HOSTILE, 1, 1);
        return super.hurtHitEntity(level, target);
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


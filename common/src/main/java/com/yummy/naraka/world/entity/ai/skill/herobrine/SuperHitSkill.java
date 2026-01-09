package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ai.skill.ComboSkill;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class SuperHitSkill extends ComboSkill<Herobrine> {
    public static final Identifier LOCATION = skillIdentifier("herobrine.super_hit");
    private int onGroundTick = 0;
    private final Set<LivingEntity> hitEntities = new HashSet<>();

    private final Supplier<Vec3> floatingBlockMovement = () -> new Vec3(0, mob.getRandom().nextDouble() * 0.3 + 0.1, 0);

    public SuperHitSkill(Herobrine mob, Skill<?> comboSkill) {
        super(LOCATION, mob, 40, 0, 1, 40, comboSkill);
        this.shieldCooldown = 60;
        this.shieldDamage = 15;
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
        runAfter(5, () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, 6));
        runAfter(5, () -> this.stopOnGround(level));
    }

    private void superHit() {
        mob.shakeCamera();
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
            NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 5 + onGroundTick, floatingBlockMovement);
            onGroundTick += 1;
        }
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(5, () -> rotateTowardTarget(target));
    }

    @Override
    protected boolean hurtEntity(ServerLevel level, LivingEntity target) {
        if (hitEntities.contains(target))
            return true;
        return super.hurtEntity(level, target);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        hitEntities.add(target);
        mob.stigmatizeEntity(level, target);
        level.playSound(mob, mob.blockPosition(), SoundEvents.STONE_BREAK, SoundSource.HOSTILE, 1, 1);
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


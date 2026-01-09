package com.yummy.naraka.world.entity.ai.skill.naraka_pickaxe;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.NarakaPickaxe;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class StrikeSkill extends AttackSkill<NarakaPickaxe> {
    public static final Identifier LOCATION = skillIdentifier("naraka_pickaxe.strike");
    private static final int STRIKE_TICK = 20;
    private static final int STRIKE_END_TICK = 160;

    public StrikeSkill(NarakaPickaxe mob) {
        super(LOCATION, mob, 200, 0);
    }

    private Supplier<Vec3> calculateFloatingMovement(float base) {
        return () -> new Vec3(0, mob.getRandom().nextDouble() * 0.2 + base, 0);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    private float calculateStigmaDamage(LivingEntity target) {
        float distance = mob.distanceTo(target);
        if (distance > 11)
            return 0;
        if (distance > 7)
            return target.getMaxHealth() * 0.1f;
        if (distance > 4)
            return target.getMaxHealth() * 0.25f;
        return target.getMaxHealth() * 0.5f;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return false;
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        int stigma = StigmaHelper.get(target).value();
        StigmaHelper.removeStigma(target);
        DamageSource damageSource = NarakaDamageSources.stigmaConsume(mob);
        target.hurtServer(level, damageSource, stigma * calculateStigmaDamage(target));
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        level.sendParticles(NarakaFlameParticleOption.EMERALD, mob.getX(), mob.getY(), mob.getZ(), 10, 0.5, 1, 0.5, 0.3);
        runBefore(STRIKE_TICK, () -> sendParticles(level));
        runAfter(STRIKE_TICK, () -> mob.setDeltaMovement(0, -8, 0));
        if (tickCount < STRIKE_END_TICK && mob.onGround())
            tickCount = STRIKE_END_TICK;

        runAt(STRIKE_END_TICK, () -> level.playSound(null, mob, SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1));
        runAt(STRIKE_END_TICK, () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, 13));
        runAt(STRIKE_END_TICK, () -> NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 5, NarakaUtils.CIRCLE, calculateFloatingMovement(0.7f)));
        runAt(STRIKE_END_TICK, () -> NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 7, NarakaUtils.CIRCLE, calculateFloatingMovement(0.4f)));
        runAt(STRIKE_END_TICK, () -> NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 13, NarakaUtils.CIRCLE, calculateFloatingMovement(0.3f)));
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        mob.discard();
    }

    private void sendParticles(ServerLevel level) {
        level.sendParticles(NarakaFlameParticleOption.EMERALD, mob.getX(), mob.getY(), mob.getZ(), 20, 0.3, 0.3, 0.3, 0.6);
    }
}

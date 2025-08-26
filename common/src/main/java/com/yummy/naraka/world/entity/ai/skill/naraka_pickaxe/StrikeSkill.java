package com.yummy.naraka.world.entity.ai.skill.naraka_pickaxe;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.NarakaPickaxe;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class StrikeSkill extends AttackSkill<NarakaPickaxe> {
    public static final ResourceLocation LOCATION = createLocation("naraka_pickaxe.strike");

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
        if (distance > 13)
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
        runBefore(40, () -> sendHaloParticles(level, 4));
        runBefore(40, () -> sendHaloParticles(level, 10));
        runBefore(40, () -> sendHaloParticles(level, 13));
        runAfter(40, () -> mob.setDeltaMovement(0, -8, 0));
        if (tickCount < 160 && mob.onGround())
            tickCount = 160;

        runAt(160, () -> level.playSound(null, mob, SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1));
        runAt(160, () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, 13));
        runAt(160, () -> NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 5, NarakaUtils.CIRCLE, calculateFloatingMovement(0.7f)));
        runAt(160, () -> NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 7, NarakaUtils.CIRCLE, calculateFloatingMovement(0.4f)));
        runAt(160, () -> NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 13, NarakaUtils.CIRCLE, calculateFloatingMovement(0.3f)));
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        mob.discard();
    }

    private void sendHaloParticles(ServerLevel level, double radius) {
        for (int angle = 0; angle < 360; angle++) {
            double x = Math.cos(Math.toRadians(angle)) * radius + mob.getX();
            double z = Math.sin(Math.toRadians(angle)) * radius + mob.getZ();
            double y = mob.getEyeY();
            level.sendParticles(NarakaFlameParticleOption.EMERALD, true, true, x, y, z, 0, 0, 0, 0, 1);
        }
    }
}

package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class StormSkill extends Skill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final.storm");
    private final HashMap<LivingEntity, Integer> hurtEntities = new HashMap<>();

    public StormSkill(Herobrine mob) {
        super(LOCATION, 80, 160, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        hurtEntities.clear();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void skillTick(ServerLevel level) {
        runAt(30, () -> pullEntities(level));

        runFrom(40, () -> stigmatizingWave(level, 40));
        runFrom(50, () -> stigmatizingWave(level, 50));
        runFrom(60, () -> stigmatizingWave(level, 60));
    }

    private boolean entityToPull(LivingEntity target) {
        return targetInRange(target, 20 * 20) && AbstractHerobrine.isNotHerobrine(target);
    }

    private void pullEntities(ServerLevel level) {
        AABB boundingBox = mob.getBoundingBox().inflate(20, 3, 20);
        level.getEntitiesOfClass(LivingEntity.class, boundingBox, this::entityToPull).forEach(target -> {
            Vec3 movement = mob.position().subtract(target.position())
                    .scale(0.3);
            target.setDeltaMovement(movement);
            if (target instanceof ServerPlayer player)
                NarakaEntityUtils.sendPlayerMovement(player, movement);
        });
    }

    private boolean findValidTarget(LivingEntity target, int startTick) {
        if (!hurtEntities.containsKey(target))
            return AbstractHerobrine.isNotHerobrine(target);
        return hurtEntities.get(target) < startTick;
    }

    private boolean inHurtRange(LivingEntity target, float radius) {
        float distanceSqr = radius * radius;
        return targetInRange(distanceSqr + 9) && targetOutOfRange(target, distanceSqr - 9);
    }

    private void stigmatizingWave(ServerLevel level, int startTick) {
        int waveTick = tickCount - startTick;
        if (waveTick == 0) {
            level.playSound(null, mob, SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 2, 1);
        }
        NarakaSkillUtils.sendCircleParticle(level, mob.position(), NarakaParticleTypes.CORRUPTED_FIRE_FLAME.get(), waveTick);
        if (waveTick > 3) {
            level.getEntitiesOfClass(
                    LivingEntity.class,
                    mob.getBoundingBox().inflate(waveTick, 3, waveTick),
                    target -> findValidTarget(target, startTick) && inHurtRange(target, waveTick)
            ).forEach(entity -> {
                mob.stigmatizeEntity(level, entity);
                hurtEntities.put(entity, startTick);
                DamageSource damageSource = mob.damageSources().magic();
                entity.hurtServer(level, damageSource, 5);
            });
        }
    }
}

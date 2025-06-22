package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

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
        runFrom(40, () -> stigmatizingWave(level, 40));
        runFrom(45, () -> stigmatizingWave(level, 45));
        runFrom(50, () -> stigmatizingWave(level, 50));
    }

    private boolean findValidTarget(LivingEntity target, int startTick) {
        if (!hurtEntities.containsKey(target))
            return AbstractHerobrine.isNotHerobrine(target);
        return hurtEntities.get(target) < startTick;
    }

    private void stigmatizingWave(ServerLevel level, int startTick) {
        int waveTick = tickCount - startTick;
        if (waveTick == 0) {
            NarakaSkillUtils.sendParticleWave(level, mob.position(), NarakaParticleTypes.CORRUPTED_FIRE_FLAME.get(), 1);
            level.playSound(null, mob, SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 2, 1);
        }
        int inflate = Math.max(waveTick - 4, 0);
        if (waveTick < 20) {
            level.getEntitiesOfClass(
                    LivingEntity.class,
                    mob.getBoundingBox().inflate(inflate, 3, inflate),
                    target -> findValidTarget(target, startTick)
            ).forEach(entity -> {
                mob.stigmatizeEntity(level, entity);
                hurtEntities.put(entity, startTick);
                DamageSource damageSource = mob.damageSources().magic();
                entity.hurtServer(level, damageSource, 5);
            });
        }
    }
}

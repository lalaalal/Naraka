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
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class StormSkill extends ComboSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final.storm");
    private final HashMap<LivingEntity, Integer> hurtEntities = new HashMap<>();

    public StormSkill(Herobrine mob, Skill<?> parryingSkill) {
        super(LOCATION, mob, 100, 600, 0.5f, 100, parryingSkill);
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
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(30, () -> NarakaSkillUtils.pullEntities(level, mob, this::entityToPull, 0.23));

        runFrom(40, () -> stigmatizingWave(level, 40));
        runFrom(50, () -> stigmatizingWave(level, 50));
        runFrom(60, () -> stigmatizingWave(level, 60));
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(30, () -> lookTarget(target));
        runBefore(30, () -> rotateTowardTarget(target));
        runAfter(60, () -> lookTarget(target));
    }

    private boolean entityToPull(LivingEntity target) {
        return targetInRange(target, 80 * 80) && AbstractHerobrine.isNotHerobrine(target);
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
                    mob.getBoundingBox().inflate(waveTick, 10, waveTick),
                    target -> findValidTarget(target, startTick) && inHurtRange(target, waveTick)
            ).forEach(target -> {
                mob.stigmatizeEntity(level, target);
                hurtEntities.put(target, startTick);
                DamageSource damageSource = mob.damageSources().magic();
                target.hurtServer(level, damageSource, calculateDamage(target));
            });
        }
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }
}

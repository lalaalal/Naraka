package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.network.AddBeamEffectPacket;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.NarakaPickaxe;
import com.yummy.naraka.world.entity.ai.skill.ComboSkill;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.data.BeamEffectsHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class StormSkill extends ComboSkill<Herobrine> {
    public static final Identifier LOCATION = createLocation("final_herobrine.storm");
    private final HashMap<LivingEntity, Integer> hurtEntities = new HashMap<>();

    public StormSkill(Herobrine mob, Skill<?> parryingSkill) {
        super(LOCATION, mob, 80, 600, 0.8f, 80, parryingSkill);
    }

    @Override
    public void prepare() {
        super.prepare();
        hurtEntities.clear();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        BlockPos blockPos = mob.blockPosition();
        BlockPos floor = NarakaUtils.findFloor(level, blockPos);
        return mob.getTarget() != null && blockPos.getY() - floor.getY() <= 2;
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(20, () -> BeamEffectsHelper.send(mob.players(), AddBeamEffectPacket.BeamEffectType.PULL, mob, 0xffff0000));
        runAt(30, () -> NarakaSkillUtils.pullLivingEntities(level, mob, this::entityToPull, 0.23));

        runFrom(40, () -> stigmatizingWave(level, 40, tickCount - 40));
        runFrom(50, () -> stigmatizingWave(level, 50, tickCount - 50));

        runBetween(50, 60, () -> sendCircleParticles(level));
        runAt(60, () -> BeamEffectsHelper.send(mob.players(), AddBeamEffectPacket.BeamEffectType.PUSH, mob, 0xffff0000));
        runAt(60, () -> BeamEffectsHelper.send(mob.players(), AddBeamEffectPacket.BeamEffectType.SIMPLE, mob, 0xffff0000));
        runAt(60, () -> NarakaSkillUtils.pullLivingEntities(level, mob, this::entityToPush, -3));
        runFrom(65, () -> stigmatizingWave(level, 65, tickCount - 70));
    }

    private void sendCircleParticles(ServerLevel level) {
        for (double angle = 0; angle < Math.TAU; angle += Math.PI / 360) {
            double x = Math.cos(angle) * 3 + mob.getX();
            double z = Math.sin(angle) * 3 + mob.getZ();
            level.sendParticles(NarakaFlameParticleOption.REDSTONE, x, mob.getY() + 0.1, z, 0, 0, 0, 0, 0);
        }
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(30, () -> lookTarget(target));
        runBefore(30, () -> rotateTowardTarget(target));
        runAfter(60, () -> lookTarget(target));
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        if (!hasLinkedSkill())
            mob.getSkillManager().waitNextSkill(40);
    }

    private boolean entityToPull(LivingEntity target) {
        return targetInRange(target, 80 * 80) && AbstractHerobrine.isNotHerobrine(target) && NarakaPickaxe.isNotNarakaPickaxe(target);
    }

    private boolean entityToPush(LivingEntity target) {
        return targetInRange(target, 9) && AbstractHerobrine.isNotHerobrine(target) && NarakaPickaxe.isNotNarakaPickaxe(target);
    }

    private boolean findValidTarget(LivingEntity target, int startTick) {
        if (!hurtEntities.containsKey(target))
            return AbstractHerobrine.isNotHerobrine(target);
        return hurtEntities.get(target) < startTick;
    }

    private boolean inHurtRange(LivingEntity target, float radius) {
        float from = (radius - 1) * (radius - 1);
        float to = (radius + 1) * (radius + 1);
        double horizontalDistance = mob.position().horizontal()
                .distanceToSqr(target.position().horizontal());
        return from < horizontalDistance && horizontalDistance < to;
    }

    private void stigmatizingWave(ServerLevel level, int startTick, int distance) {
        if (distance == 0) {
            level.playSound(null, mob, SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 2, 1);
        }
        NarakaSkillUtils.sendCircleParticle(level, mob.position(), NarakaFlameParticleOption.REDSTONE, distance);
        if (distance > 3) {
            level.getEntitiesOfClass(
                    LivingEntity.class,
                    mob.getBoundingBox().inflate(distance, 10, distance),
                    target -> findValidTarget(target, startTick) && inHurtRange(target, distance)
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
        return target.getMaxHealth() * 0.2f;
    }
}

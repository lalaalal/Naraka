package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.*;
import com.yummy.naraka.world.entity.ai.skill.ComboSkill;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class StormSkill extends ComboSkill<Herobrine> {
    public static final Identifier LOCATION = skillIdentifier("final_herobrine.storm");
    private final HashMap<LivingEntity, Integer> hurtEntities = new HashMap<>();

    public StormSkill(Herobrine mob, Skill<?> parryingSkill) {
        super(LOCATION, mob, 80, 900, 0.8f, 80, parryingSkill);
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
        double floorDistance = blockPos.getY() - floor.getY();
        return mob.getTarget() != null && floorDistance <= 2 && floorDistance > 1;
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(30, () -> NarakaSkillUtils.pullLivingEntities(level, mob, this::entityToPull, 0.23));

        runAt(20, () -> {
            ShinyEffect shinyEffect = new ShinyEffect(level, 30, false, 1f, 0, SoulType.REDSTONE.color);
            shinyEffect.setPos(mob.getEyePosition());
            level.addFreshEntity(shinyEffect);
        });
        runAt(30, () -> createAreaEffect(level));
        runFrom(40, () -> stigmatizingWave(level, 40, tickCount - 40));
        runFrom(50, () -> stigmatizingWave(level, 50, tickCount - 50));

        runBetween(60, 66, () -> ShinyEffect.spawnShinySpark(level, mob.position(), mob.getRandom(), 6, 60, SoulType.REDSTONE.color));
        runAt(60, () -> NarakaSkillUtils.pullLivingEntities(level, mob, this::entityToPush, -3));
        runFrom(65, () -> stigmatizingWave(level, 65, tickCount - 65));
    }

    private void createAreaEffect(ServerLevel level) {
        float y = NarakaUtils.findFloor(level, mob.blockPosition()).getY() + 1;
        Vec3 position = new Vec3(mob.getX(), y, mob.getZ());
        level.addFreshEntity(new AreaEffect(level, position, 50, 3, 6, 0xff0000, 0));
        level.addFreshEntity(new AreaEffect(level, position, 50, 4.5f, 4.5f, 0xff0000, 1));
        level.addFreshEntity(new AreaEffect(level, position, 50, 6, 3, 0xff0000, 2));
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
        return targetInRange(target, 9) && AbstractHerobrine.isNotHerobrine(target)
                && NarakaPickaxe.isNotNarakaPickaxe(target)
                && !NarakaEntityUtils.disableAndHurtShield(target, 200, 5);
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

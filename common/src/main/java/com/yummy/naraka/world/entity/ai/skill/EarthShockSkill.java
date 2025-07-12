package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.LightningCircle;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.ColoredLightningBolt;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.MassiveLightning;
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

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class EarthShockSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final_herobrine.earth_shock");

    private static final float HALF_ANGLE = Mth.HALF_PI * 0.125f;

    public EarthShockSkill(Herobrine mob) {
        super(LOCATION, mob, 240, 600);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.5f;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    private Supplier<Vec3> blockFloatingMovement(float base, float multiplier) {
        return () -> new Vec3(0, mob.getRandom().nextFloat() * base * multiplier + base, 0);
    }

    private UnaryOperator<Vec3> applyAcceleration() {
        int moveTick = tickCount - 40;
        double multiplier = Math.pow(0.9, moveTick);
        double currentSpeed = multiplier * 4;
        double dy = multiplier * 5;

        return position -> position.normalize().scale(currentSpeed).add(0, dy, 0);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(40, () -> rotateTowardTarget(target));
        runBetween(40, 90, () -> moveToTarget(target, false, applyAcceleration()));
    }

    private Predicate<LivingEntity> targetBetween(float from, float to) {
        return target -> targetInRange(to * to)
                && targetOutOfRange(from * from)
                && AbstractHerobrine.isNotHerobrine(target);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        if (target == null) {
            runBetween(40, 90, () -> mob.setDeltaMovement(0, 4, 0));
            runBetween(40, 90, () -> reduceSpeed(0.8));
        }

        runBetween(60, 90, () -> sendParticles(level, 10));
        runBetween(90, 95, () -> spawnLightningBolts(level, 3, 9, 0xaaff00ff));
        runBetween(90, 100, () -> shockwaveBlocks(level, 90, 3, -Mth.PI, Mth.PI, blockFloatingMovement(0.3f, 0.4f)));
        runAt(90, mob::shakeCamera);
        runAt(92, () -> hurtEntities(level, targetBetween(1, 10), 10));
        runBetween(100, 125, () -> hurtTargetInAngle(level, 100, 2, -HALF_ANGLE, HALF_ANGLE));
        runBetween(100, 125, () -> hurtTargetInAngle(level, 95, 1, -HALF_ANGLE + Mth.HALF_PI, HALF_ANGLE + Mth.HALF_PI));
        runBetween(100, 125, () -> hurtTargetInAngle(level, 95, 1, -HALF_ANGLE - Mth.HALF_PI, HALF_ANGLE - Mth.HALF_PI));
        runBetween(100, 125, () -> hurtTargetInAngle(level, 95, 1, -HALF_ANGLE + Mth.PI, HALF_ANGLE + Mth.PI));
        runAt(90, () -> mob.setDeltaMovement(0, -8, 0));

        runAt(125, () -> spawnMassiveLightning(level));
        runAt(125, mob::shakeCamera);
        runBetween(125, 135, () -> shockwaveBlocks(level, 125, 3, -Mth.PI, Mth.PI, blockFloatingMovement(0.3f, 0.4f)));
        runAt(125, () -> hurtEntities(level, targetBetween(1, 10), 10));
        runBetween(170, 180, () -> sendParticles(level, 5));

        runBetween(125, 130, () -> spawnLightningBolts(level, 4, 10, 0x99ff00ff));
        runAt(145, () -> NarakaSkillUtils.pullEntities(level, mob, this::entityToPull, 0.25));

        runAt(172, mob::shakeCamera);
        runBetween(172, 176, () -> shockwaveBlocks(level, 172, 3, -Mth.PI, Mth.PI, blockFloatingMovement(0.3f, 0.4f)));
        runAt(172, () -> level.playSound(null, mob, SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1));
        runAt(172, () -> hurtEntities(level, targetBetween(1, 10), 10));
        runAt(170, () -> spawnLightningCircle(level));
        runBetween(170, 180, () -> sendParticles(level, 5));

        runAt(180, () -> spawnLightningCircle(level));
        runAt(200, () -> spawnLightningCircle(level));

        runAt(200, () -> mob.setDeltaMovement(0, 0.4, 0));
        runAfter(200, () -> reduceSpeed(0.6));
    }

    private void spawnMassiveLightning(ServerLevel level) {
        MassiveLightning massiveLightning = new MassiveLightning(level, mob, 10);
        massiveLightning.setPos(mob.position());
        level.addFreshEntity(massiveLightning);
        mob.shakeCamera();
    }

    private void spawnLightningCircle(ServerLevel level) {
        level.addFreshEntity(new LightningCircle(level, mob));
    }

    private boolean entityToPull(LivingEntity target) {
        return targetInRange(target, 80 * 80) && AbstractHerobrine.isNotHerobrine(target);
    }

    private void sendParticles(ServerLevel level, int maxRadius) {
        float y = NarakaUtils.findFloor(level, mob.blockPosition()).getY() + 1.1f;
        for (int i = 0; i < 100; i++) {
            double angle = mob.getRandom().nextFloat() * Math.TAU;
            double radius = mob.getRandom().nextFloat() * maxRadius * 2 - maxRadius;
            double x = Math.cos(angle) * radius + mob.getX();
            double z = Math.sin(angle) * radius + mob.getZ();
            level.sendParticles(ParticleTypes.FLAME, x, y, z, 0, 0, 1, 0, 0.05);
        }
    }

    private void spawnLightningBolts(ServerLevel level, int count, int maxRadius, int color) {
        float y = NarakaUtils.findFloor(level, mob.blockPosition()).getY() + 1.1f;
        for (int i = 0; i < count; i++) {
            double angle = mob.getRandom().nextFloat() * Math.TAU;
            double x = Math.cos(angle) * maxRadius + mob.getX() + mob.getRandom().nextDouble();
            double z = Math.sin(angle) * maxRadius + mob.getZ() + mob.getRandom().nextDouble();
            ColoredLightningBolt lightningBolt = new ColoredLightningBolt(level, new Vec3(x, y, z), color);
            lightningBolt.setVisualOnly(true);
            level.addFreshEntity(lightningBolt);
        }
    }

    private void shockwaveBlocks(ServerLevel level, int startTick, int radius, float angleFrom, float angleTo, Supplier<Vec3> movementSupplier) {
        int waveTick = tickCount - startTick;
        level.sendParticles(ParticleTypes.FIREWORK, mob.getX(), mob.getY(), mob.getZ(), 15, 0.5, 1, 0.5, 0.3);

        BiPredicate<BlockPos, Integer> predicate = (position, r) -> {
            BlockPos actualPosition = mob.blockPosition().offset(position);
            return targetInLookAngle(new Vec3(actualPosition.getX() + 0.5, mob.getY(), actualPosition.getZ() + 0.5), angleFrom, angleTo)
                    && NarakaUtils.OUTLINE.test(position, r);
        };
        NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), radius + waveTick, predicate, movementSupplier);
    }

    private void hurtTargetInAngle(ServerLevel level, int startTick, int tickInterval, float angleFrom, float angleTo) {
        int rawSpawnTick = tickCount - startTick;
        if (rawSpawnTick % tickInterval != 0)
            return;

        int spawnTick = rawSpawnTick / tickInterval;
        shockwaveBlocks(level, startTick, 1, angleFrom, angleTo, blockFloatingMovement(0.2f, 0.05f * spawnTick));

        float distanceFrom = spawnTick - 1;
        float distanceTo = spawnTick + 1;
        Predicate<LivingEntity> targetPredicate = target -> targetInLookAngle(target, angleFrom, angleTo)
                && targetOutOfRange(target, distanceFrom * distanceFrom)
                && targetInRange(target, distanceTo * distanceTo)
                && AbstractHerobrine.isNotHerobrine(target);
        hurtEntities(level, targetPredicate, spawnTick + 4);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
    }
}

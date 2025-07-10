package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.ColoredLightningBolt;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.MassiveLightning;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class EarthShockSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("earth_shock");

    private static final float HALF_ANGLE = Mth.HALF_PI * 0.125f;

    public EarthShockSkill(Herobrine mob) {
        super(LOCATION, mob, 205, 600);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    private Supplier<Vec3> blockFloatingMovement(float base, float multiplier) {
        return () -> new Vec3(0, mob.getRandom().nextFloat() * base * multiplier + base, 0);
    }

    private UnaryOperator<Vec3> applyAcceleration(int startTick, double speed, double ySpeed, double acceleration) {
        int moveTick = tickCount - startTick;
        double multiplier = Math.pow(acceleration, moveTick);
        double currentSpeed = multiplier * speed;
        double dy = multiplier * ySpeed;

        return position -> position.normalize().scale(currentSpeed).add(0, dy, 0);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(10, () -> rotateTowardTarget(target));
        runBetween(10, 60, () -> moveToTarget(target, false, applyAcceleration(10, 4, 5, 0.9f)));

    }

    private Predicate<LivingEntity> targetBetween(float from, float to) {
        return target -> targetInRange(to * to)
                && targetOutOfRange(from * from)
                && AbstractHerobrine.isNotHerobrine(target);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        if (target == null) {
            runBetween(10, 60, () -> mob.setDeltaMovement(0, 4, 0));
            runBetween(10, 60, () -> reduceSpeed(0.8));
        }

        runBetween(30, 60, () -> sendParticles(level, 10));
        runBetween(60, 65, () -> spawnLightningBolts(level, 3, 9, 0x335A1D8D));
        runBetween(60, 70, () -> shockwaveBlocks(level, 60, 3, -Mth.PI, Mth.PI, blockFloatingMovement(0.3f, 0.4f)));
        runAt(62, () -> hurtEntities(level, targetBetween(1, 10), 10));
        runBetween(65, 95, () -> hurtTargetInAngle(level, 65, 1, 1, -HALF_ANGLE, HALF_ANGLE));
        runBetween(65, 95, () -> hurtTargetInAngle(level, 65, 1, 1, -HALF_ANGLE + Mth.HALF_PI, HALF_ANGLE + Mth.HALF_PI));
        runBetween(65, 95, () -> hurtTargetInAngle(level, 65, 1, 1, -HALF_ANGLE - Mth.HALF_PI, HALF_ANGLE - Mth.HALF_PI));
        runBetween(65, 95, () -> hurtTargetInAngle(level, 65, 1, 1, -HALF_ANGLE + Mth.PI, HALF_ANGLE + Mth.PI));
        runAt(60, () -> mob.setDeltaMovement(0, -8, 0));

        runAt(95, () -> spawnMassiveLightning(level));
        runBetween(95, 100, () -> spawnLightningBolts(level, 4, 10, 0x335A1D8D));

        runAt(115, () -> NarakaSkillUtils.pullEntities(level, mob, this::entityToPull, 0.25));

        runAt(170, () -> mob.setDeltaMovement(0, 0.4, 0));
        runAfter(170, () -> reduceSpeed(0.6));
    }

    private void spawnMassiveLightning(ServerLevel level) {
        MassiveLightning massiveLightning = new MassiveLightning(level, mob, 10);
        massiveLightning.setPos(mob.position());
        level.addFreshEntity(massiveLightning);
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

    private void hurtTargetInAngle(ServerLevel level, int startTick, int tickInterval, float positionInterval, float angleFrom, float angleTo) {
        int rawSpawnTick = tickCount - startTick;
        if (rawSpawnTick % tickInterval != 0)
            return;

        int spawnTick = rawSpawnTick / tickInterval;
        shockwaveBlocks(level, 65, 1, angleFrom, angleTo, blockFloatingMovement(0.2f, 0.05f * spawnTick));

        float distance = spawnTick * positionInterval;
        float distanceFrom = distance - positionInterval;
        float distanceTo = distance + positionInterval;
        Predicate<LivingEntity> targetPredicate = target -> targetInLookAngle(target, angleFrom, angleTo)
                && targetOutOfRange(target, distanceFrom * distanceFrom)
                && targetInRange(target, distanceTo * distanceTo)
                && AbstractHerobrine.isNotHerobrine(target);
        hurtEntities(level, targetPredicate, distance + 4);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
    }
}

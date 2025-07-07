package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.ColoredLightningBolt;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EarthShockSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("earth_shock");

    private int color;

    public EarthShockSkill(Herobrine mob) {
        super(LOCATION, mob, 125, 240);
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

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(10, () -> rotateTowardTarget(target));
        runBetween(10, 15, () -> NarakaSkillUtils.moveToTarget(target, mob, 0.3, -2));
        runAfter(90, () -> lookTarget(target));
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(0, () -> mob.setDeltaMovement(0, 8, 0));
        runBetween(0, 10, () -> reduceSpeed(0.4));
        runAt(10, () -> mob.setDeltaMovement(0, -8, 0));
        color = ARGB.color(0x33, 0x0000E5);
        runBetween(15, 25, () -> spawnLightningBoltGroup(level, 15, 3, 1.4f, -Mth.PI, Mth.PI, Mth.PI * 0.1f));
        runBetween(20, 25, () -> shockwaveBlocks(level, 15, 1, -Mth.PI, Mth.PI, blockFloatingMovement(0.2f, 0.4f)));
        color = ARGB.color(0x33, 0x940000);
        runBetween(15, 30, () -> spawnLightningBoltGroup(level, 15, 1, 1f, -Mth.HALF_PI * 0.125f, Mth.HALF_PI * 0.125f, Mth.PI * 0.1f));

        color = ARGB.color(0x33, 0x11118D);
        runBetween(30, 60, () -> spawnLightningBoltGroup(level, 30, 3, 1.2f, -Mth.PI, Mth.PI, 0.314f * 2));
        color = ARGB.color(0x33, 0x94118D);
        runBetween(30, 45, () -> spawnLightningBoltGroup(level, 30, 1, 1f, Mth.HALF_PI * 0.125f, Mth.HALF_PI * 0.75f, Mth.PI * 0.1f));
        runBetween(30, 45, () -> spawnLightningBoltGroup(level, 30, 1, 1f, -Mth.HALF_PI * 0.75f, -Mth.HALF_PI * 0.125f, Mth.PI * 0.1f));
        runBetween(30, 45, () -> shockwaveBlocks(level, 30, 1, Mth.HALF_PI * 0.25f, Mth.HALF_PI * 0.75f, blockFloatingMovement(0.3f, (tickCount - 30) * 0.1f)));
        runBetween(30, 45, () -> shockwaveBlocks(level, 30, 1, -Mth.HALF_PI * 0.75f, -Mth.HALF_PI * 0.25f, blockFloatingMovement(0.3f, (tickCount - 30) * 0.1f)));

        color = ARGB.color(0x33, 0x5A118D);
        runBetween(55, 100, () -> spawnLightningBoltGroup(level, 55, 1, 2f, -Mth.HALF_PI * 0.125f, Mth.HALF_PI * 0.125f, 0.314f / 4));
        runBetween(55, 80, () -> shockwaveBlocks(level, 55, 2, -Mth.HALF_PI * 0.125f, Mth.HALF_PI * 0.125f, blockFloatingMovement(0.3f, (tickCount - 55) * 0.15f)));
        runAt(88, () -> mob.setDeltaMovement(0, 0.4, 0));
        runAfter(88, () -> reduceSpeed(0.6));
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

    private void spawnLightningBoltGroup(ServerLevel level, int startTick, int tickInterval, float positionInterval, float angleFrom, float angleTo, float angleInterval) {
        int rawSpawnTick = tickCount - startTick;
        if (rawSpawnTick % tickInterval != 0)
            return;

        int spawnTick = rawSpawnTick / tickInterval;
        float distance = spawnTick * positionInterval;
        for (float angle = angleFrom; angle <= angleTo; angle += angleInterval) {
            Vec3 direction = mob.getLookAngle().yRot(angle);
            spawnLightningBolt(level, direction, distance, positionInterval);
        }

        float distanceFrom = distance - positionInterval;
        float distanceTo = distance + positionInterval;
        Predicate<LivingEntity> targetPredicate = target -> targetInLookAngle(target, angleFrom, angleTo)
                && targetOutOfRange(target, distanceFrom * distanceFrom)
                && targetInRange(target, distanceTo * distanceTo)
                && AbstractHerobrine.isNotHerobrine(target);
        hurtEntities(level, targetPredicate, distance + 4);
    }

    private void spawnLightningBolt(ServerLevel level, Vec3 direction, float distance, float offset) {
        float offsetX = (mob.getRandom().nextFloat() * 2 - 1) * offset;
        float offsetZ = (mob.getRandom().nextFloat() * 2 - 1) * offset;
        Vec3 position = mob.position()
                .add(direction.scale(distance))
                .add(offsetX, 0, offsetZ);
        ColoredLightningBolt lightningBolt = new ColoredLightningBolt(level, position, color);
        lightningBolt.setVisualOnly(true);
        level.addFreshEntity(lightningBolt);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
    }
}

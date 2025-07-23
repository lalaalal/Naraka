package com.yummy.naraka.util;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.world.entity.StigmatizingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NarakaSkillUtils {
    public static void shockwaveBlocks(Level level, BlockPos base, int radius, BiPredicate<BlockPos, Integer> positionPredicate, Supplier<Vec3> movementSupplier) {
        base = NarakaUtils.findAir(level, base, Direction.UP);

        NarakaUtils.square(base, radius, positionPredicate, blockPos -> {
            BlockPos floor = NarakaUtils.findFloor(level, blockPos);
            BlockState state = level.getBlockState(floor);
            if (level.getBlockState(floor.above()).isAir())
                NarakaEntityUtils.createFloatingBlock(level, floor.above(), state, movementSupplier.get());
        });
    }

    public static void shockwaveBlocks(Level level, BlockPos base, int radius, Supplier<Vec3> movementSupplier) {
        shockwaveBlocks(level, base, radius, NarakaUtils.CIRCLE_OUTLINE, movementSupplier);
    }

    public static <T extends LivingEntity & StigmatizingEntity> void stigmatize(ServerLevel level, T mob, int radius) {
        Collection<LivingEntity> entities = level.getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.forCombat(),
                mob,
                mob.getBoundingBox().inflate(radius)
        );
        for (LivingEntity target : entities)
            mob.stigmatizeEntity(level, target);

        NarakaSkillUtils.sendParticleWave(level, mob.position(), NarakaFlameParticleOption.REDSTONE, 1);
    }

    public static void sendParticleWave(ServerLevel level, Vec3 position, ParticleOptions particle, double speed) {
        for (int yRot = 0; yRot < 360; yRot++) {
            double xSpeed = Math.cos(Math.toRadians(yRot));
            double zSpeed = Math.sin(Math.toRadians(yRot));
            level.sendParticles(particle, position.x(), position.y() + 1.5, position.z(), 0, xSpeed, 0, zSpeed, speed);
        }
    }

    public static void sendParticleFront(ServerLevel level, Mob mob, LivingEntity target, ParticleOptions particle) {
        Vec3 deltaNormal = NarakaEntityUtils.getDirectionNormalVector(mob, target);
        Vec3 position = mob.position().add(deltaNormal);
        level.sendParticles(particle, position.x, position.y + mob.getEyeHeight(), position.z, 1, 0, 0, 0, 1);
    }

    public static void sendCircleParticle(ServerLevel level, Vec3 position, ParticleOptions particle, double radius) {
        for (int yRot = 0; yRot < 360; yRot++) {
            double x = Math.cos(Math.toRadians(yRot)) * radius;
            double z = Math.sin(Math.toRadians(yRot)) * radius;
            level.sendParticles(particle, position.x() + x, position.y() + 1.5, position.z() + z, 1, 0, 0, 0, 1);
        }
    }

    public static void sendTraceParticles(ServerLevel level, LivingEntity entity, ParticleOptions particle) {
        Vec3 movement = entity.getDeltaMovement();

        int count = 25;
        for (int i = 0; i < count; i++) {
            float delta = i / (float) count;

            double x = entity.getX() + Mth.lerp(delta, 0, movement.x) + entity.getRandom().nextDouble() * 0.3;
            double y = entity.getEyeY() + 0.5 + Mth.lerp(delta, 0, movement.y) + entity.getRandom().nextDouble() * 0.1;
            double z = entity.getZ() + Mth.lerp(delta, 0, movement.z) + entity.getRandom().nextDouble() * 0.3;
            level.sendParticles(particle, x, y, z, 1, 0, 0.01, 0, 0.1);
        }
    }

    public static void pullLivingEntities(ServerLevel level, Mob mob, Predicate<LivingEntity> targetPredicate, double scale) {
        pullEntities(level, mob, LivingEntity.class, targetPredicate, scale);
    }

    public static <T extends Entity> void pullEntities(ServerLevel level, Mob mob, Class<T> type, Predicate<T> targetPredicate, double scale) {
        AABB boundingBox = mob.getBoundingBox().inflate(80, 10, 80);
        level.getEntitiesOfClass(type, boundingBox, targetPredicate).forEach(target -> {
            Vec3 movement = mob.position().subtract(target.position())
                    .scale(scale);
            target.setDeltaMovement(movement);
            if (target instanceof ServerPlayer player)
                NarakaEntityUtils.sendPlayerMovement(player, movement);
        });
    }
}

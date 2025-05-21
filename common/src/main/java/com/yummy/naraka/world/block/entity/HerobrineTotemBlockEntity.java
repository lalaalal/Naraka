package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.data.worldgen.NarakaStructures;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class HerobrineTotemBlockEntity extends BlockEntity {
    private static final IntegerProperty CRACK = HerobrineTotem.CRACK;
    private static final int MAX_CRACK = HerobrineTotem.MAX_CRACK;

    private int tickCount = 1;
    private int waitCount = 6;
    private int particleCount = 0;

    public HerobrineTotemBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public HerobrineTotemBlockEntity(BlockPos pos, BlockState state) {
        this(NarakaBlockEntityTypes.HEROBRINE_TOTEM.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, HerobrineTotemBlockEntity blockEntity) {
        blockEntity.serverTick((ServerLevel) level, pos, state);
    }

    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (isSleeping(state))
            return;

        int crack = state.getValue(CRACK);
        if (crack == MAX_CRACK - 1 && tickCount % 5 == 0) {
            waitCount -= 1;
        }
        boolean shouldWait = crack == MAX_CRACK - 1 && waitCount > 0;

        if (crack >= MAX_CRACK - 2 && particleCount < 35) {
            particleCount += 1;
            level.sendParticles(NarakaParticleTypes.HEROBRINE_SPAWN.get(),
                    pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1,
                    0, 0.2, 0, 0.01
            );
            level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS);
        }
        if (tickCount % 5 == 0 && level.random.nextFloat() < 0.25f && !shouldWait) {
            if (crack == MAX_CRACK) {
                breakTotemStructure(level, pos);
                summonHerobrine(level, pos);
            } else {
                HerobrineTotem.crack(level, pos, state);
            }
            if (level.random.nextFloat() < 0.9f) {
                level.sendParticles(ParticleTypes.SMOKE,
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 10,
                        0.5, 0.5, 0.5, 0
                );
                level.sendParticles(ParticleTypes.FLAME,
                        pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 20,
                        1, 1, 1, 0
                );
                level.playSound(null, pos, SoundEvents.NETHER_BRICKS_BREAK, SoundSource.BLOCKS);
            }
        }
        tickCount += 1;
    }

    public static boolean isActivated(BlockState state) {
        return 0 < state.getValue(CRACK);
    }

    public static boolean isSleeping(BlockState state) {
        return !isActivated(state);
    }

    public static boolean isTotemStructure(Level level, BlockPos totemPos) {
        return level.getBlockState(totemPos).is(NarakaBlocks.HEROBRINE_TOTEM.get())
                && level.getBlockState(totemPos.above()).is(Blocks.NETHERRACK)
                && level.getBlockState(totemPos.below(1)).is(NarakaBlocks.IMITATION_GOLD_BLOCK)
                && level.getBlockState(totemPos.below(2)).is(NarakaBlocks.IMITATION_GOLD_BLOCK);
    }

    public static boolean isSanctuaryExists(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            HolderLookup<Structure> structures = serverLevel.holderLookup(Registries.STRUCTURE);
            LocationPredicate predicate = LocationPredicate.Builder.inStructure(
                            structures.getOrThrow(NarakaStructures.HEROBRINE_SANCTUARY)
                    )
                    .build();
            return predicate.matches(serverLevel, pos.getX(), pos.getY(), pos.getZ());
        }
        return false;
    }

    private void summonHerobrine(ServerLevel level, BlockPos pos) {
        BlockPos floorPos = NarakaUtils.findFloor(level, pos);
        Herobrine herobrine = new Herobrine(level, new Vec3(floorPos.getX() + 0.5, floorPos.getY() + 1, floorPos.getZ() + 0.5));
        herobrine.setSpawnPosition(pos);
        herobrine.setAnimation(AnimationLocations.BLOCKING);
        level.addFreshEntity(herobrine);

        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        lightningBolt.setVisualOnly(true);
        lightningBolt.setPos(herobrine.position());
        level.addFreshEntity(lightningBolt);

        level.sendParticles(ParticleTypes.CLOUD,
                pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 15,
                1, 1, 1, 0.01
        );
        level.sendParticles(ParticleTypes.FLAME,
                pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 30,
                1, 1, 1, 0.05
        );

        List<ServerPlayer> players = level.getEntities(EntityTypeTest.forExactClass(ServerPlayer.class), AABB.ofSize(NarakaUtils.vec3(pos), 16, 16, 16), entity -> true);
        for (ServerPlayer player : players)
            CriteriaTriggers.SUMMONED_ENTITY.trigger(player, herobrine);
    }

    private void breakTotemStructure(Level level, BlockPos pos) {
        if (isTotemStructure(level, pos)) {
            level.destroyBlock(pos.above(), false);
            level.destroyBlock(pos.below(), false);
            level.destroyBlock(pos.below(2), false);
        }
        level.destroyBlock(pos, false);
    }
}

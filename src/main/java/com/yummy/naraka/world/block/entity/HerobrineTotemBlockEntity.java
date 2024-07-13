package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class HerobrineTotemBlockEntity extends BlockEntity {
    private static final IntegerProperty CRACK = HerobrineTotem.CRACK;
    private static final int MAX_CRACK = HerobrineTotem.MAX_CRACK;

    private int tickCount = 1;

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
        if (isSleeping(state)) {
            if (isTotemStructure(level, pos) && refineFire(level, pos))
                crack(level, pos, state);
            return;
        }

        if (tickCount % 10 == 0) {
            if (state.getValue(CRACK) == MAX_CRACK) {
                summonHerobrine(level, pos);
                breakTotemStructure(level, pos);
            } else
                crack(level, pos, state);
            if (level.random.nextFloat() < 0.8f) {
                level.sendParticles(ParticleTypes.CLOUD,
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 25,
                        0.5, 0.5, 0.5, 0.01
                );
                level.playSound(null, pos, SoundEvents.NETHER_BRICKS_BREAK, SoundSource.BLOCKS);
            }
        }
        tickCount += 1;
    }

    private boolean isActivated(BlockState state) {
        return 0 < state.getValue(CRACK);
    }

    private boolean isSleeping(BlockState state) {
        return !isActivated(state);
    }

    private void crack(Level level, BlockPos pos, BlockState state) {
        int crack = state.getValue(CRACK);
        if (crack < MAX_CRACK)
            level.setBlock(pos, state.setValue(CRACK, crack + 1), Block.UPDATE_ALL_IMMEDIATE);
    }

    public boolean isTotemStructure(Level level, BlockPos totemPos) {
        return level.getBlockState(totemPos).is(NarakaBlocks.HEROBRINE_TOTEM)
                && level.getBlockState(totemPos.above()).is(Blocks.NETHERRACK)
                && level.getBlockState(totemPos.below(1)).is(NarakaBlocks.FAKE_GOLD_BLOCK)
                && level.getBlockState(totemPos.below(2)).is(NarakaBlocks.FAKE_GOLD_BLOCK);
    }

    public boolean refineFire(Level level, BlockPos totemPos) {
        if (level.getBlockState(totemPos.above(2)).is(BlockTags.FIRE)) {
            level.setBlock(totemPos.above(2), NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get().defaultBlockState(), Block.UPDATE_CLIENTS);
            return true;
        }
        return false;
    }

    private void summonHerobrine(ServerLevel level, BlockPos pos) {
        Herobrine herobrine = new Herobrine(level, new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
        level.addFreshEntity(herobrine);
        level.sendParticles(ParticleTypes.CLOUD,
                pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 40,
                1, 1, 1, 0.01
        );

        List<ServerPlayer> players = level.getEntities(EntityTypeTest.forExactClass(ServerPlayer.class), AABB.ofSize(NarakaUtils.vec3(pos), 8, 8, 8), entity -> true);
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

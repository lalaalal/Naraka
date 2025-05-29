package com.yummy.naraka.world.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class EbonyLeavesBlock extends LeavesBlock {
    public static final MapCodec<EbonyLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(mangroveLeavesBlock -> mangroveLeavesBlock.leafParticleChance),
                            propertiesCodec()
                    )
                    .apply(instance, EbonyLeavesBlock::new)
    );

    public EbonyLeavesBlock(float leafParticleChance, Properties properties) {
        super(leafParticleChance, properties);
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
        ParticleUtils.spawnParticleBelow(level, pos, random, NarakaParticleTypes.EBONY_LEAVES.get());
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        super.animateTick(blockState, level, blockPos, randomSource);
        if (randomSource.nextInt(10) == 0) {
            BlockPos blockPos2 = blockPos.below();
            BlockState blockState2 = level.getBlockState(blockPos2);
            if (!isFaceFull(blockState2.getCollisionShape(level, blockPos2), Direction.UP)) {
                ParticleUtils.spawnParticleBelow(level, blockPos, randomSource, NarakaParticleTypes.EBONY_LEAVES.get());
            }
        }
    }
}

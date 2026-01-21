package com.yummy.naraka.world.block;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.world.block.entity.HerobrineTotemBlockEntity;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jspecify.annotations.Nullable;

public class HerobrineTotem extends BaseEntityBlock {
    private static final MapCodec<HerobrineTotem> CODEC = simpleCodec(HerobrineTotem::new);

    public static final int MAX_CRACK = 11;
    public static final IntegerProperty CRACK = IntegerProperty.create("crack", 0, MAX_CRACK);

    public static int light(BlockState state) {
        return state.getValue(CRACK);
    }

    public static void crack(Level level, BlockPos pos, BlockState state) {
        int crack = state.getValue(CRACK);
        if (crack == 0)
            level.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS);
        if (crack < MAX_CRACK)
            level.setBlock(pos, state.setValue(CRACK, crack + 1), UPDATE_ALL_IMMEDIATE);
    }

    public HerobrineTotem(Properties properties) {
        super(properties);
        this.registerDefaultState(
                getStateDefinition().any()
                        .setValue(CRACK, 0)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CRACK);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        level.getBlockEntity(pos, NarakaBlockEntityTypes.HEROBRINE_TOTEM.get()).ifPresent(blockEntity -> {
            blockEntity.setCustomPlaced(true);
        });
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HerobrineTotemBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide())
            return super.getTicker(level, state, type);
        return createTickerHelper(type, NarakaBlockEntityTypes.HEROBRINE_TOTEM.get(), HerobrineTotemBlockEntity::serverTick);
    }
}

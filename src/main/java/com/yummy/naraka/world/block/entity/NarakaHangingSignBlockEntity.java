package com.yummy.naraka.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;

public class NarakaHangingSignBlockEntity extends HangingSignBlockEntity {
    public NarakaHangingSignBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return NarakaBlockEntities.EBONY_HANGING_SIGN.get();
    }

    @Override
    protected SignText createDefaultSignText() {
        return super.createDefaultSignText().setColor(DyeColor.WHITE);
    }
}

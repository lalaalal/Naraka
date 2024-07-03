package com.yummy.naraka.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;

public class NarakaSignBlockEntity extends SignBlockEntity {
    public NarakaSignBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(NarakaBlockEntityTypes.EBONY_SIGN.get(), pPos, pBlockState);
    }

    public NarakaSignBlockEntity(BlockEntityType pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    protected SignText createDefaultSignText() {
        return super.createDefaultSignText().setColor(DyeColor.WHITE);
    }
}

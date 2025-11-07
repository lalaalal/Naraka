package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.world.NarakaDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NarakaPortalBlockEntity extends BlockEntity {
    private int usage = 3;

    public NarakaPortalBlockEntity(BlockPos pos, BlockState blockState) {
        super(NarakaBlockEntityTypes.NARAKA_PORTAL.get(), pos, blockState);
    }

    public void use() {
        if (level != null && level.dimension() != NarakaDimensions.NARAKA) {
            usage -= 1;
            if (usage <= 0)
                level.destroyBlock(getBlockPos(), false);
            setChanged();
        }
    }

    public int getUsage() {
        if (level != null && level.dimension() == NarakaDimensions.NARAKA)
            return 1;
        return usage;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag compoundTag = super.getUpdateTag(registries);
        compoundTag.putInt("Usage", usage);
        return compoundTag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("Usage", usage);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("Usage")) {
            usage = tag.getInt("Usage");
        } else {
            usage = 1;
        }
    }
}

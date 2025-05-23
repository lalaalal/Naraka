package com.yummy.naraka.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class UnstableBlockEntity extends BlockEntity {
    private BlockState original = Blocks.DIRT.defaultBlockState();

    public UnstableBlockEntity(BlockPos pos, BlockState blockState) {
        super(NarakaBlockEntityTypes.UNSTABLE_BLOCK.get(), pos, blockState);
    }

    public void setOriginal(BlockState original) {
        this.original = original;
    }

    public BlockState getOriginal() {
        return original;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.put("Original", NbtUtils.writeBlockState(original));

        return tag;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Original"))
            this.original = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), tag.getCompoundOrEmpty("Original"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Original", NbtUtils.writeBlockState(original));
    }
}

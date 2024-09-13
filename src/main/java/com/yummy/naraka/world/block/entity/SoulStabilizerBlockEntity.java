package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SoulStabilizerBlockEntity extends BlockEntity {
    public static final int CAPACITY = 15552;

    @Nullable
    private SoulType soulType;
    private int souls = 0;

    public SoulStabilizerBlockEntity(BlockPos pos, BlockState blockState) {
        super(NarakaBlockEntityTypes.SOUL_STABILIZER_BLOCK_ENTITY, pos, blockState);
    }

    public boolean canInject(ItemStack itemStack) {
        return (soulType == null || soulType.test(itemStack))
                && souls + getSoulByItem(itemStack) <= CAPACITY;
    }

    private int getSoulByItem(ItemStack itemStack) {
        if (soulType != null) {
            if (soulType.item() == itemStack.getItem())
                return 1;
            if (soulType.block().asItem() == itemStack.getItem())
                return 9;
        }
        return 0;
    }

    public void inject(ItemStack itemStack) {
        if (soulType == null)
            soulType = SoulType.fromItem(itemStack);
        this.souls += getSoulByItem(itemStack);
        setChanged();
    }

    @Nullable
    public SoulType getSoulType() {
        return soulType;
    }

    public int getSouls() {
        return souls;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag compoundTag = super.getUpdateTag(registries);
        if (soulType != null) {
            compoundTag.putString("SoulType", soulType.toString());
            compoundTag.putInt("Souls", souls);
        }
        return compoundTag;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (soulType != null) {
            tag.putString("SoulType", soulType.toString());
            tag.putInt("Souls", souls);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("SoulType")) {
            soulType = SoulType.valueOf(tag.getString("SoulType"));
            souls = tag.getInt("Souls");
        }
    }
}

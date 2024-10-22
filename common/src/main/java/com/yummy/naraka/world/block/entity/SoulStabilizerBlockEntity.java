package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SoulStabilizerBlockEntity extends BlockEntity {
    public static final int CAPACITY = 15552;

    @Nullable
    private SoulType soulType;
    private int souls = 0;

    public SoulStabilizerBlockEntity(BlockPos pos, BlockState blockState) {
        super(NarakaBlockEntityTypes.SOUL_STABILIZER.get(), pos, blockState);
    }

    public boolean canInject(ItemStack itemStack) {
        if (soulType == null)
            return SoulType.fromItem(itemStack) != null;
        return soulType.test(itemStack) && souls + getSoulByItem(itemStack) <= CAPACITY;
    }

    private int findMaxInjectableCount(ItemStack itemStack) {
        int itemSoul = getSoulByItem(itemStack);
        if (itemSoul == 0)
            return 0;
        return Mth.clamp((CAPACITY - souls) / itemSoul, 0, itemStack.getCount());
    }

    private int getSoulByItem(ItemStack itemStack) {
        if (soulType != null) {
            if (soulType == SoulType.GOD_BLOOD)
                return 9 * 64;
            if (soulType.getItem() == itemStack.getItem())
                return 1;
            if (soulType.getBlockItem() == itemStack.getItem())
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

    public int injectAll(ItemStack itemStack) {
        if (soulType == null)
            soulType = SoulType.fromItem(itemStack);
        int count = findMaxInjectableCount(itemStack);
        this.souls += getSoulByItem(itemStack) * count;
        setChanged();
        return count;
    }

    public int tryInject(ItemStack itemStack, boolean injectAll) {
        if (injectAll)
            return injectAll(itemStack);
        inject(itemStack);
        return 1;
    }

    @Nullable
    public SoulType getSoulType() {
        return soulType;
    }

    public int getSouls() {
        return souls;
    }

    public void consumeSoul(int consume) {
        souls = Mth.clamp(souls - consume, 0, CAPACITY);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
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

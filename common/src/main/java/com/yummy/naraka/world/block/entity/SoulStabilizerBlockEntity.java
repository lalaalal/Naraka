package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.advancements.NarakaCriteriaTriggers;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoulStabilizerBlockEntity extends BlockEntity {
    private SoulType soulType = SoulType.NONE;
    private int souls = 0;

    public static int getCapacity() {
        return NarakaConfig.COMMON.soulStabilizerCapacity.getValue();
    }

    public static int getConsume() {
        return NarakaConfig.COMMON.soulStabilizerConsume.getValue();
    }

    public SoulStabilizerBlockEntity(BlockPos pos, BlockState blockState) {
        super(NarakaBlockEntityTypes.SOUL_STABILIZER.get(), pos, blockState);
    }

    public void reset() {
        soulType = SoulType.NONE;
        souls = 0;
    }

    public boolean canInject(ItemStack itemStack) {
        if (soulType == SoulType.NONE)
            return SoulType.fromItem(itemStack) != SoulType.NONE;
        return soulType.test(itemStack) && souls + getSoulByItem(itemStack) <= getCapacity();
    }

    private int findMaxInjectableCount(ItemStack itemStack) {
        int itemSoul = getSoulByItem(itemStack);
        if (itemSoul == 0)
            return 0;
        return Mth.clamp((getCapacity() - souls) / itemSoul, 0, itemStack.getCount());
    }

    private int getSoulByItem(ItemStack itemStack) {
        if (soulType != SoulType.NONE) {
            if (soulType == SoulType.GOD_BLOOD)
                return getCapacity();
            if (soulType.getItem() == itemStack.getItem())
                return 1;
            if (soulType.getBlockItem() == itemStack.getItem())
                return 9;
        }
        return 0;
    }

    public int inject(ItemStack itemStack, boolean injectAll) {
        if (soulType == SoulType.NONE)
            soulType = SoulType.fromItem(itemStack);
        int count = injectAll ? findMaxInjectableCount(itemStack) : 1;
        this.souls += getSoulByItem(itemStack) * count;
        setChanged();
        return count;
    }

    /**
     * @param player    Player injecting
     * @param itemStack Injecting item
     * @param injectAll Use all stacks
     * @return Used count of item
     */
    public int tryInject(Player player, ItemStack itemStack, boolean injectAll) {
        int count = inject(itemStack, injectAll);
        if (player instanceof ServerPlayer serverPlayer)
            NarakaCriteriaTriggers.FILL_SOUL_STABILIZER.get().trigger(serverPlayer, souls == getCapacity());
        return count;
    }

    public void clear() {
        soulType = SoulType.NONE;
        souls = 0;
        setChanged();
    }

    public SoulType getSoulType() {
        return soulType;
    }

    public int getSouls() {
        return souls;
    }

    public void consumeSoul(int consume) {
        souls = Mth.clamp(souls - consume, 0, getCapacity());
        if (souls == 0)
            soulType = SoulType.NONE;
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
        NarakaNbtUtils.store(compoundTag, "SoulType", SoulType.CODEC, soulType);
        compoundTag.putInt("Souls", souls);
        return compoundTag;
    }

    @Override
    protected void saveAdditional(CompoundTag output, HolderLookup.Provider registries) {
        NarakaNbtUtils.store(output, "SoulType", SoulType.CODEC, soulType);
        output.putInt("Souls", souls);
    }

    @Override
    protected void loadAdditional(CompoundTag input, HolderLookup.Provider registries) {
        soulType = NarakaNbtUtils.read(input, "SoulType", SoulType.CODEC).orElse(SoulType.NONE);
        souls = input.getInt("Souls");
    }
}

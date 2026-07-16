package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ForgingBlockEntity extends BlockEntity {
    public static final int COOLDOWN = 30;

    private final float successChance;
    protected ItemStack forgingItem = ItemStack.EMPTY;
    protected int cooldownTick = 0;

    protected ForgingBlockEntity(BlockEntityType<? extends ForgingBlockEntity> type, BlockPos blockPos, BlockState blockState, float successChance) {
        super(type, blockPos, blockState);
        this.successChance = successChance;
    }

    public boolean canReinforce(ItemStack stack) {
        if (level == null)
            return false;
        return Reinforcement.canReinforce(stack, level.registryAccess());
    }

    public void setForgingItem(ItemStack forgingItem) {
        this.forgingItem = forgingItem.copy();
        setChanged();
    }

    public void dropForgingItem() {
        if (level != null && !forgingItem.isEmpty()) {
            NarakaItemUtils.summonItemEntity(level, forgingItem, getBlockPos());
            forgingItem = ItemStack.EMPTY;
            setChanged();
        }
    }

    public void dropItems() {
        dropForgingItem();
    }

    public ItemStack getForgingItem() {
        return forgingItem;
    }

    public boolean tryReinforce(Player player) {
        if (level == null || level.isClientSide()
                || !Reinforcement.canReinforce(forgingItem, level.registryAccess())
                || cooldownTick > 0)
            return false;
        if (level.random.nextFloat() < successChance) {
            if (Reinforcement.increase(forgingItem, NarakaReinforcementEffects.byItem(forgingItem), level.registryAccess()))
                level.playSound(null, getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS);
            setChanged();
        } else {
            level.playSound(null, getBlockPos(), SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS);
        }
        cooldownTick = COOLDOWN;
        return true;
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
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        if (!forgingItem.isEmpty() && level != null)
            NarakaNbtUtils.store(compoundTag, "ForgingItem", ItemStack.CODEC, RegistryOps.create(NbtOps.INSTANCE, level.registryAccess()), forgingItem);
        return compoundTag;
    }

    @Override
    public void load(CompoundTag input) {
        super.load(input);
        if (level != null)
            forgingItem = NarakaNbtUtils.read(input, "ForgingItem", ItemStack.CODEC, RegistryOps.create(NbtOps.INSTANCE, level.registryAccess()))
                    .orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(CompoundTag output) {
        if (!forgingItem.isEmpty() && level != null)
            NarakaNbtUtils.store(output, "ForgingItem", ItemStack.CODEC, RegistryOps.create(NbtOps.INSTANCE, level.registryAccess()), forgingItem);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ForgingBlockEntity blockEntity) {
        if (blockEntity.cooldownTick > 0)
            blockEntity.cooldownTick -= 1;
    }
}

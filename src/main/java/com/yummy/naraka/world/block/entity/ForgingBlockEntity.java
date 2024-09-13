package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class ForgingBlockEntity extends BlockEntity {
    public static final float SUCCESS_CHANCE = 0.5f;

    private ItemStack itemStack = ItemStack.EMPTY;
    private int cooldownTick = 0;

    public ForgingBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NarakaBlockEntityTypes.FORGING_BLOCK_ENTITY, blockPos, blockState);

    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack.copy();
        setChanged();
    }

    public void dropItem() {
        if (level != null && !itemStack.isEmpty()) {
            level.addFreshEntity(new ItemEntity(
                    level,
                    getBlockPos().getX(),
                    getBlockPos().getY() + 1,
                    getBlockPos().getZ(),
                    itemStack
            ));
            itemStack = ItemStack.EMPTY;
            setChanged();
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean tryReinforce() {
        if (itemStack.isEmpty() || !Reinforcement.canReinforce(itemStack)
                || level == null || level.isClientSide
                || cooldownTick > 0)
            return false;
        if (level.random.nextFloat() < SUCCESS_CHANCE) {
            if (Reinforcement.increase(itemStack, NarakaReinforcementEffects.byItem(itemStack)))
                level.playSound(null, getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS);
            setChanged();
        } else {
            level.playSound(null, getBlockPos(), SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS);
        }
        cooldownTick = 30;
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        if (!itemStack.isEmpty())
            compoundTag.put("ForgingItem", itemStack.save(provider));
        return compoundTag;
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        Optional<ItemStack> optional = ItemStack.parse(provider, compoundTag.get("ForgingItem"));
        optional.ifPresentOrElse(stack -> itemStack = stack, () -> itemStack = ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if (!itemStack.isEmpty()) {
            compoundTag.put("ForgingItem", itemStack.save(provider));
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ForgingBlockEntity blockEntity) {
        if (blockEntity.cooldownTick > 0)
            blockEntity.cooldownTick -= 1;
    }
}

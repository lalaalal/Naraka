package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class ForgingBlockEntity extends BlockEntity {
    public static final int MAX_REINFORCEMENT = 10;
    public static final float SUCCESS_CHANCE = 0.5f;

    private ItemStack itemStack = ItemStack.EMPTY;

    public ForgingBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NarakaBlockEntityTypes.FORGING_BLOCK_ENTITY, blockPos, blockState);

    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack.copy();
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
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void tryReinforce() {
        if (itemStack.isEmpty() || !Reinforcement.canReinforce(itemStack)
                || level == null || level.isClientSide)
            return;
        if (level.random.nextFloat() < SUCCESS_CHANCE) {
            if (Reinforcement.increase(itemStack, NarakaReinforcementEffects.get(itemStack)))
                level.playSound(null, getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS);
        } else {
            level.playSound(null, getBlockPos(), SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS);
        }
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
}

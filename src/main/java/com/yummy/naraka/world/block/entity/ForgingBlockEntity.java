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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class ForgingBlockEntity extends BlockEntity {
    public static final float DEFAULT_SUCCESS_CHANCE = 0.3f;

    private final float successChance;
    private final Predicate<ItemStack> itemPredicate;
    private ItemStack forgingItem = ItemStack.EMPTY;
    private int cooldownTick = 0;

    public ForgingBlockEntity(BlockPos pos, BlockState state) {
        this(NarakaBlockEntityTypes.FORGING, pos, state, DEFAULT_SUCCESS_CHANCE, itemStack -> true);
    }

    protected ForgingBlockEntity(BlockEntityType<? extends ForgingBlockEntity> type, BlockPos blockPos, BlockState blockState, float successChance, Predicate<ItemStack> itemPredicate) {
        super(type, blockPos, blockState);
        this.successChance = successChance;
        this.itemPredicate = itemPredicate;
    }

    public void setForgingItem(ItemStack forgingItem) {
        this.forgingItem = forgingItem.copy();
        setChanged();
    }

    public void dropItem() {
        if (level != null && !forgingItem.isEmpty()) {
            level.addFreshEntity(new ItemEntity(
                    level,
                    getBlockPos().getX(),
                    getBlockPos().getY() + 1,
                    getBlockPos().getZ(),
                    forgingItem
            ));
            forgingItem = ItemStack.EMPTY;
            setChanged();
        }
    }

    public ItemStack getForgingItem() {
        return forgingItem;
    }

    public boolean tryReinforce() {
        if (forgingItem.isEmpty() || !itemPredicate.test(forgingItem)
                || !Reinforcement.canReinforce(forgingItem)
                || level == null || level.isClientSide
                || cooldownTick > 0)
            return false;
        if (level.random.nextFloat() < successChance) {
            if (Reinforcement.increase(forgingItem, NarakaReinforcementEffects.byItem(forgingItem)))
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
        if (!forgingItem.isEmpty())
            compoundTag.put("ForgingItem", forgingItem.save(provider));
        return compoundTag;
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        forgingItem = ItemStack.parseOptional(provider, compoundTag.getCompound("ForgingItem"));
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if (!forgingItem.isEmpty()) {
            compoundTag.put("ForgingItem", forgingItem.save(provider));
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ForgingBlockEntity blockEntity) {
        if (blockEntity.cooldownTick > 0)
            blockEntity.cooldownTick -= 1;
    }
}

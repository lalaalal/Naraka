package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SoulSmithingBlockEntity extends ForgingBlockEntity {
    private final SoulStabilizerBlockEntity soulStabilizer;
    private boolean isStabilizerAttached;

    public SoulSmithingBlockEntity(BlockPos pos, BlockState state) {
        this(NarakaBlockEntityTypes.SOUL_SMITHING, pos, state, 1, itemStack -> itemStack.is(NarakaItemTags.PURIFIED_SOUL_ARMORS));
    }

    protected SoulSmithingBlockEntity(BlockEntityType<? extends SoulSmithingBlockEntity> type, BlockPos pos, BlockState state, float successChance, Predicate<ItemStack> itemPredicate) {
        super(type, pos, state, successChance, itemPredicate);
        soulStabilizer = new SoulStabilizerBlockEntity(pos, NarakaBlocks.SOUL_STABILIZER.defaultBlockState());
    }

    public boolean isStabilizerAttached() {
        return isStabilizerAttached;
    }

    @Nullable
    public SoulType getSoulType() {
        if (isStabilizerAttached)
            return soulStabilizer.getSoulType();
        return null;
    }

    public int getSouls() {
        if (isStabilizerAttached)
            return soulStabilizer.getSouls();
        return 0;
    }

    public boolean tryAttachSoulStabilizer(ItemStack stack) {
        if (!isStabilizerAttached && stack.is(NarakaBlocks.SOUL_STABILIZER.asItem())) {
            soulStabilizer.applyComponentsFromItemStack(stack);
            isStabilizerAttached = true;
            setChanged();
            return true;
        }
        return false;
    }

    public void detachSoulStabilizer() {
        if (isStabilizerAttached && level != null) {
            ItemStack itemStack = new ItemStack(NarakaBlocks.SOUL_STABILIZER);
            soulStabilizer.saveToItem(itemStack, level.registryAccess());
            level.addFreshEntity(new ItemEntity(
                    level,
                    getBlockPos().getX(),
                    getBlockPos().getY() + 1,
                    getBlockPos().getZ(),
                    itemStack
            ));

            isStabilizerAttached = false;
            setChanged();
        }
    }

    @Override
    public boolean tryReinforce() {
        if (isStabilizerAttached)
            return super.tryReinforce();
        return false;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.putBoolean("IsStabilizerAttached", isStabilizerAttached);
        if (isStabilizerAttached) {
            CompoundTag stabilizerTag = soulStabilizer.getUpdateTag(provider);
            tag.put("StabilizerData", stabilizerTag);
        }

        return tag;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putBoolean("IsStabilizerAttached", isStabilizerAttached);
        if (isStabilizerAttached) {
            CompoundTag stabilizerTag = new CompoundTag();
            compoundTag.put("StabilizerData", stabilizerTag);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        isStabilizerAttached = compoundTag.getBoolean("IsStabilizerAttached");
        if (isStabilizerAttached) {
            CompoundTag stabilizerTag = compoundTag.getCompound("StabilizerData");
            soulStabilizer.loadAdditional(stabilizerTag, provider);
        }
    }
}

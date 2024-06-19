package com.yummy.naraka.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoulCraftingBlockEntity extends BaseContainerBlockEntity {
    public SoulCraftingBlockEntity(BlockPos pos, BlockState blockState) {
        super(NarakaBlockEntities.SOUL_CRAFTING_BLOCK_ENTITY.get(), pos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return null;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {

    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }
}

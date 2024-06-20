package com.yummy.naraka.block.entity;

import com.yummy.naraka.world.inventory.SoulCraftingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoulCraftingBlockEntity extends BaseContainerBlockEntity {
    public static final int FUEL_SLOT = 0;
    public static final int INGREDIENT_SLOT = 1;

    public static final int FUEL_DATA_ID = 0;
    public static final int CRAFTING_TIME_DATA_ID = 1;

    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private final ContainerData data = new SimpleContainerData(2);

    public SoulCraftingBlockEntity(BlockPos pos, BlockState blockState) {
        super(NarakaBlockEntities.SOUL_CRAFTING_BLOCK_ENTITY.get(), pos, blockState);
    }

    public int getFuel() {
        return data.get(FUEL_DATA_ID);
    }

    public int getCraftingTime() {
        return data.get(CRAFTING_TIME_DATA_ID);
    }

    public void setFuel(int fuel) {
        data.set(FUEL_DATA_ID, fuel);
    }

    public void setCraftingTime(int craftingTime) {
        data.set(CRAFTING_TIME_DATA_ID, craftingTime);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.soul_crafting");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SoulCraftingMenu(containerId, inventory, this, data);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SoulCraftingBlockEntity blockEntity) {
        int fuel = blockEntity.getFuel();
        int craftingTime = blockEntity.getCraftingTime();
    }
}
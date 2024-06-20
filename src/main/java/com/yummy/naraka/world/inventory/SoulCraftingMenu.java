package com.yummy.naraka.world.inventory;

import com.yummy.naraka.block.entity.SoulCraftingBlockEntity;
import com.yummy.naraka.item.soul.SoulCrafting;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SoulCraftingMenu extends AbstractContainerMenu {
    private final Inventory inventory;
    private final Container container;
    private final ContainerData data;

    protected SoulCraftingMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(3), new SimpleContainerData(2));
    }

    public SoulCraftingMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(NarakaMenuTypes.SOUL_CRAFTING.get(), containerId);
        checkContainerSize(container, 3);
        checkContainerDataCount(data, 2);
        this.inventory = inventory;
        this.container = container;
        this.data = data;

        Slot fuelSlot = PredicateSlot.builder(container)
                .at(SoulCraftingBlockEntity.FUEL_SLOT)
                .position(10, 10)
                .with(SoulCrafting::isFuel)
                .build();
        Slot ingredientSlot = PredicateSlot.builder(container)
                .at(SoulCraftingBlockEntity.INGREDIENT_SLOT)
                .position(5, 20)
                .with(SoulCrafting::isCraftable)
                .build();
        addSlot(fuelSlot);
        addSlot(ingredientSlot);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}

package com.yummy.naraka.world.inventory;

import com.yummy.naraka.block.entity.SoulCraftingBlockEntity;
import com.yummy.naraka.item.NarakaItems;
import com.yummy.naraka.item.crafting.NarakaRecipeTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class SoulCraftingMenu extends AbstractContainerMenu {
    public static final int FUEL_SLOT = 0;
    public static final int INGREDIENT_SLOT = 1;
    public static final int RESULT_SLOT = 2;

    public static final int SLOT_SIZE = 3;

    public static final int FUEL_DATA_ID = 0;
    public static final int LIT_PROGRESS_DATA_ID = 1;
    public static final int CRAFTING_PROGRESS_DATA_ID = 2;

    public static final int DATA_SIZE = 3;

    public static final int PLAYER_INVENTORY_START = 3;
    public static final int PLAYER_INVENTORY_SIZE = 27;
    public static final int PLAYER_INVENTORY_END = PLAYER_INVENTORY_START + PLAYER_INVENTORY_SIZE;
    public static final int PLAYER_HOTBAR_START = PLAYER_INVENTORY_END;
    public static final int PLAYER_HOTBAR_END = PLAYER_HOTBAR_START + 9;

    private final Container container;
    private final ContainerData data;
    private final Level level;
    private final RecipeManager recipeManager;

    protected SoulCraftingMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOT_SIZE), new SimpleContainerData(DATA_SIZE));
    }

    public SoulCraftingMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(NarakaMenuTypes.SOUL_CRAFTING.get(), containerId);
        checkContainerSize(container, SLOT_SIZE);
        checkContainerDataCount(data, DATA_SIZE);
        this.container = container;
        this.data = data;
        this.level = inventory.player.level();
        this.recipeManager = level.getRecipeManager();

        Slot fuelSlot = PredicateSlot.builder(container)
                .at(FUEL_SLOT)
                .position(26, 47)
                .with(this::isFuel)
                .build();
        Slot ingredientSlot = PredicateSlot.builder(container)
                .at(INGREDIENT_SLOT)
                .position(56, 17)
                .with(this::isIngredient)
                .build();
        addSlot(fuelSlot);
        addSlot(ingredientSlot);
        addSlot(new Slot(container, RESULT_SLOT, 116, 35));
        addDataSlots(data);

        for (int row = 0; row < 3; row++) {
            for (int columm = 0; columm < 9; columm++) {
                addSlot(new Slot(inventory, columm + row * 9 + 9, 8 + columm * 18, 84 + row * 18));
            }
        }

        for (int row = 0; row < 9; row++) {
            addSlot(new Slot(inventory, row, 8 + row * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        ItemStack item = slot.getItem();
        ItemStack copy = item.copy();

        if (PLAYER_INVENTORY_START <= index) {
            if (isIngredient(item)) {
                if (!moveItemStackTo(item, INGREDIENT_SLOT, INGREDIENT_SLOT + 1, false))
                    return ItemStack.EMPTY;
            } else if (isFuel(item)) {
                if (!moveItemStackTo(item, FUEL_SLOT, FUEL_SLOT + 1, false))
                    return ItemStack.EMPTY;
            } else if (index < PLAYER_INVENTORY_END && !moveItemStackTo(item, PLAYER_HOTBAR_START, PLAYER_HOTBAR_END, false))
                return ItemStack.EMPTY;
            else if (PLAYER_HOTBAR_START <= index && index < PLAYER_HOTBAR_END && !moveItemStackTo(item, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, false))
                return ItemStack.EMPTY;
        } else {
            if (!moveItemStackTo(item, PLAYER_INVENTORY_START, PLAYER_HOTBAR_END, false))
                return ItemStack.EMPTY;
        }

        if (item.isEmpty())
            slot.setByPlayer(ItemStack.EMPTY);
        else
            slot.setChanged();

        slot.onTake(player, item);
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public boolean isIngredient(ItemStack itemStack) {
        SingleRecipeInput input = new SingleRecipeInput(itemStack);
        return recipeManager.getRecipeFor(NarakaRecipeTypes.SOUL_CRAFTING.get(), input, level).isPresent();
    }

    public boolean isFuel(ItemStack itemStack) {
        return itemStack.is(NarakaItems.PURIFIED_SOUL_SHARD);
    }

    public boolean isCrafting() {
        ItemStack ingredient = slots.get(INGREDIENT_SLOT).getItem();
        int fuel = data.get(FUEL_DATA_ID);
        return fuel == SoulCraftingBlockEntity.requiredFuels() && isIngredient(ingredient);
    }

    public double getCraftingProgress() {
        double progress = Math.min(data.get(CRAFTING_PROGRESS_DATA_ID), SoulCraftingBlockEntity.craftingTime());
        return progress / (double) SoulCraftingBlockEntity.craftingTime();
    }

    public double getLitProgress() {
        return (double) data.get(LIT_PROGRESS_DATA_ID) / (double) SoulCraftingBlockEntity.craftingTime();
    }

    public double getFuelCharge() {
        return (double) data.get(FUEL_DATA_ID) / (double) SoulCraftingBlockEntity.requiredFuels();
    }
}

package com.yummy.naraka.world.inventory;

import com.yummy.naraka.block.entity.SoulCraftingBlockEntity;
import com.yummy.naraka.item.crafting.SoulCraftingRecipe;
import com.yummy.naraka.item.soul.SoulCrafting;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class SoulCraftingMenu extends RecipeBookMenu<SingleRecipeInput, SoulCraftingRecipe> {
    private final Inventory inventory;
    private final Container container;
    private final ContainerData data;
    private final Level level;
    private final RecipeManager recipeManager;

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
        this.level = inventory.player.level();
        this.recipeManager = level.getRecipeManager();

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

    @Override
    public void fillCraftSlotsStackedContents(StackedContents helper) {
        if (container instanceof StackedContentsCompatible stackedContentsCompatible)
            stackedContentsCompatible.fillStackedContents(helper);
    }

    @Override
    public void clearCraftingContent() {
        getSlot(SoulCraftingBlockEntity.INGREDIENT_SLOT).set(ItemStack.EMPTY);
        getSlot(SoulCraftingBlockEntity.RESULT_SLOT).set(ItemStack.EMPTY);
    }

    private SingleRecipeInput getRecipeInput() {
        return new SingleRecipeInput(container.getItem(SoulCraftingBlockEntity.INGREDIENT_SLOT));
    }

    @Override
    public boolean recipeMatches(RecipeHolder<SoulCraftingRecipe> recipe) {
        return recipe.value().matches(getRecipeInput(), level);
    }

    @Override
    public int getResultSlotIndex() {
        return SoulCraftingBlockEntity.RESULT_SLOT;
    }

    @Override
    public int getGridWidth() {
        return 1;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return container.getContainerSize();
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int pSlotIndex) {
        return false;
    }
}

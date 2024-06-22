package com.yummy.naraka.block.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.block.SoulCraftingBlock;
import com.yummy.naraka.item.crafting.NarakaRecipeTypes;
import com.yummy.naraka.item.crafting.SoulCraftingRecipe;
import com.yummy.naraka.world.inventory.SoulCraftingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SoulCraftingBlockEntity extends BaseContainerBlockEntity implements RecipeCraftingHolder {
    public static final int FUEL_SLOT = 0;
    public static final int INGREDIENT_SLOT = 1;
    public static final int RESULT_SLOT = 2;

    public static final int FUEL_DATA_ID = 0;
    public static final int CRAFTING_TIME_DATA_ID = 1;

    private static int requiredFuels;
    private static int craftingTime;

    public static int requiredFuels() {
        return requiredFuels;
    }

    public static int craftingTime() {
        return craftingTime;
    }

    public static void loadConfig() {
        requiredFuels = NarakaMod.config().soulCraftingRequiredFuel.get();
        craftingTime = NarakaMod.config().soulCraftingTime.get();
    }

    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private final ContainerData data = new SimpleContainerData(2);
    private RecipeHolder<?> recipeUsed;

    public SoulCraftingBlockEntity(BlockPos pos, BlockState blockState) {
        super(NarakaBlockEntities.SOUL_CRAFTING_BLOCK_ENTITY.get(), pos, blockState);
        setFuel(0);
        setCraftingTime(-1);
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

        ItemStack fuelItem = blockEntity.getItem(FUEL_SLOT);
        if (!fuelItem.isEmpty() && fuel < requiredFuels()) {
            fuelItem.shrink(1);
            fuel += 1;
            blockEntity.setFuel(fuel);
        }

        ItemStack ingredientItem = blockEntity.getItem(INGREDIENT_SLOT);
        ItemStack existingResult = blockEntity.getItem(RESULT_SLOT);
        Optional<RecipeHolder<SoulCraftingRecipe>> optional = getRecipeFor(level, ingredientItem);
        if (!ingredientItem.isEmpty()
                && optional.isPresent() && canCraft(level, optional.get(), existingResult)
                && fuel == requiredFuels()
                && craftingTime == -1) {
            blockEntity.setCraftingTime(craftingTime());
            blockEntity.setFuel(0);
            level.setBlock(pos, state.setValue(SoulCraftingBlock.LIT, true), 10);
        }

        if (craftingTime == 0 && optional.isPresent()) {
            ItemStack crafted = assemble(level, optional.get(), ingredientItem);
            if (existingResult.isEmpty())
                blockEntity.setItem(RESULT_SLOT, crafted);
            else if (existingResult.is(crafted.getItem()))
                existingResult.grow(1);
            level.setBlock(pos, state.setValue(SoulCraftingBlock.LIT, false), 10);
        }
        if (craftingTime >= 0)
            blockEntity.setCraftingTime(craftingTime - 1);
    }

    public static boolean canCraft(Level level, RecipeHolder<SoulCraftingRecipe> recipe, ItemStack existingResult) {
        ItemStack result = recipe.value().getResultItem(level.registryAccess());
        return existingResult.isEmpty() || result.is(existingResult.getItem());
    }

    public static Optional<RecipeHolder<SoulCraftingRecipe>> getRecipeFor(Level level, ItemStack ingredient) {
        return level.getRecipeManager().getRecipeFor(NarakaRecipeTypes.SOUL_CRAFTING.get(), new SingleRecipeInput(ingredient), level);
    }

    public static ItemStack assemble(Level level, RecipeHolder<SoulCraftingRecipe> recipe, ItemStack ingredient) {
        ingredient.shrink(1);
        return recipe.value().assemble(new SingleRecipeInput(ingredient), level.registryAccess());
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        this.recipeUsed = recipe;
    }

    @Nullable
    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return recipeUsed;
    }
}
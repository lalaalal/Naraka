package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.world.block.SoulCraftingBlock;
import com.yummy.naraka.world.inventory.SoulCraftingMenu;
import com.yummy.naraka.world.item.crafting.NarakaRecipeTypes;
import com.yummy.naraka.world.item.crafting.SoulCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
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

    public static final int SLOT_SIZE = 3;

    public static final int FUEL_DATA_ID = 0;
    public static final int LIT_PROGRESS_DATA_ID = 1;
    public static final int CRAFTING_PROGRESS_DATA_ID = 2;

    public static final int DATA_SIZE = 3;

    public static final int PROGRESS_WAITING = 0;
    public static final int PROGRESS_START = 1;

    private static final int REQUIRED_FUELS = 10;
    private static final int CRAFTING_TIME = 20 * 30;

    public static int requiredFuels() {
        return REQUIRED_FUELS;
    }

    public static int craftingTime() {
        return CRAFTING_TIME;
    }

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_SIZE, ItemStack.EMPTY);
    private final ContainerData data = new SimpleContainerData(DATA_SIZE);
    private @Nullable RecipeHolder<?> recipeUsed;

    public SoulCraftingBlockEntity(BlockPos pos, BlockState blockState) {
        super(NarakaBlockEntityTypes.SOUL_CRAFTING_BLOCK_ENTITY, pos, blockState);
        setFuel(0);
        setCraftingProgress(PROGRESS_WAITING);
        setLitProgress(0);
    }

    public int getFuel() {
        return data.get(FUEL_DATA_ID);
    }

    public int getCraftingProgress() {
        return data.get(CRAFTING_PROGRESS_DATA_ID);
    }

    public int getLitProgress() {
        return data.get(LIT_PROGRESS_DATA_ID);
    }

    public void setFuel(int fuel) {
        data.set(FUEL_DATA_ID, fuel);
    }

    public void setCraftingProgress(int craftingProgress) {
        data.set(CRAFTING_PROGRESS_DATA_ID, Math.max(craftingProgress, PROGRESS_WAITING));
    }

    public void setLitProgress(int remaining) {
        data.set(LIT_PROGRESS_DATA_ID, Math.clamp(remaining, 0, craftingTime()));
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

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider registries) {
        super.saveAdditional(compoundTag, registries);
        ContainerHelper.saveAllItems(compoundTag, this.items, registries);
        compoundTag.putInt("LitProgress", getLitProgress());
        compoundTag.putInt("CraftingProgress", getCraftingProgress());
        compoundTag.putInt("Fuel", getFuel());
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider registries) {
        super.loadAdditional(compoundTag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items, registries);
        setLitProgress(compoundTag.getInt("LitProgress"));
        setCraftingProgress(compoundTag.getInt("CraftingProgress"));
        setFuel(compoundTag.getInt("Fuel"));
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SoulCraftingBlockEntity blockEntity) {
        int fuel = blockEntity.getFuel();
        int craftingProgress = blockEntity.getCraftingProgress();
        int litProgress = blockEntity.getLitProgress();

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
                && craftingProgress == PROGRESS_WAITING) {
            blockEntity.setCraftingProgress(PROGRESS_START);
            blockEntity.setLitProgress(craftingTime());
            blockEntity.setFuel(0);
            level.setBlock(pos, state.setValue(SoulCraftingBlock.LIT, true), 10);
            return;
        }

        if (craftingProgress == craftingTime() && optional.isPresent() && !ingredientItem.isEmpty()) {
            ItemStack crafted = assemble(level, optional.get(), ingredientItem);
            if (existingResult.isEmpty())
                blockEntity.setItem(RESULT_SLOT, crafted);
            else if (existingResult.is(crafted.getItem()))
                existingResult.grow(1);
            level.setBlock(pos, state.setValue(SoulCraftingBlock.LIT, false), 10);
            blockEntity.setCraftingProgress(PROGRESS_WAITING);
        }

        if (canProcess(craftingProgress)) {
            int increase = 1;
            if (ingredientItem.isEmpty())
                increase = Mth.ceil(-craftingTime() * 0.05);

            blockEntity.setCraftingProgress(craftingProgress + increase);
            blockEntity.setLitProgress(litProgress - Mth.abs(increase));
        }
    }

    private static boolean canProcess(int progress) {
        return PROGRESS_START <= progress && progress < craftingTime();
    }

    public static boolean canCraft(Level level, RecipeHolder<SoulCraftingRecipe> recipe, ItemStack existingResult) {
        ItemStack result = recipe.value().getResultItem(level.registryAccess());
        return existingResult.isEmpty() || result.is(existingResult.getItem());
    }

    public static Optional<RecipeHolder<SoulCraftingRecipe>> getRecipeFor(Level level, ItemStack ingredient) {
        return level.getRecipeManager().getRecipeFor(NarakaRecipeTypes.SOUL_CRAFTING, new SingleRecipeInput(ingredient), level);
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
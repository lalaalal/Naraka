package com.yummy.naraka.jei.recipe;

import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.crafting.SoulCraftingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class SoulCraftingRecipeCategory implements IRecipeCategory<SoulCraftingRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;

    public SoulCraftingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(NarakaTextures.SOUL_CRAFTING, 25, 16, 113, 49);
        this.icon = guiHelper.createDrawableItemStack(NarakaBlocks.SOUL_CRAFTING_BLOCK.asItem().getDefaultInstance());
        this.title = Component.translatable("container.soul_crafting");
    }

    @Override
    public RecipeType<SoulCraftingRecipe> getRecipeType() {
        return NarakaJeiRecipeTypes.SOUL_CRAFTING;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SoulCraftingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 31)
                .addItemStack(new ItemStack(NarakaItems.PURIFIED_SOUL_SHARD, 10));
        builder.addSlot(INPUT, 31, 1)
                .addIngredients(recipe.getIngredients().getFirst());
        builder.addSlot(OUTPUT, 91, 19)
                .addItemStack(RecipeUtil.getResultItem(recipe));
    }
}

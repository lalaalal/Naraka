package com.yummy.naraka.jei.recipe;

import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.crafting.SoulCraftingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.gui.GuiGraphics;
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
        this.icon = guiHelper.createDrawableItemStack(NarakaBlocks.SOUL_CRAFTING_BLOCK.get().asItem().getDefaultInstance());
        this.title = Component.translatable("container.soul_crafting");
    }

    @Override
    public int getWidth() {
        return 113;
    }

    @Override
    public int getHeight() {
        return 49;
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
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SoulCraftingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 31)
                .addItemStack(new ItemStack(NarakaItems.PURIFIED_SOUL_SHARD));
        builder.addSlot(INPUT, 31, 1)
                .addIngredients(recipe.getIngredients().getFirst());
        builder.addSlot(OUTPUT, 91, 19)
                .addItemStack(RecipeUtil.getResultItem(recipe));
    }

    @Override
    public void draw(SoulCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
    }
}

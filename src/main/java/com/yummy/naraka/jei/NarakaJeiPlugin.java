package com.yummy.naraka.jei;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.jei.recipe.NarakaJeiRecipeTypes;
import com.yummy.naraka.jei.recipe.NarakaRecipes;
import com.yummy.naraka.jei.recipe.SoulCraftingRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
@SuppressWarnings("unused")
public class NarakaJeiPlugin implements IModPlugin {
    public static final ResourceLocation ID = NarakaMod.location("jei", "naraka");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new SoulCraftingRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(NarakaJeiRecipeTypes.SOUL_CRAFTING, NarakaRecipes.soulCrafting());
    }
}
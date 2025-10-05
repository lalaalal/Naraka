package com.yummy.naraka.mixin.client;

import com.yummy.naraka.world.item.crafting.display.ComponentPredicateRecipeDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.recipebook.CraftingRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(CraftingRecipeBookComponent.class)
public abstract class CraftingRecipeBookComponentMixin extends RecipeBookComponent<AbstractCraftingMenu> {
    public CraftingRecipeBookComponentMixin(AbstractCraftingMenu menu, List<TabInfo> tabInfos) {
        super(menu, tabInfos);
    }

    @Inject(method = "canDisplay", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractCraftingMenu;getGridHeight()I", shift = At.Shift.AFTER), cancellable = true)
    private void canDisplayComponentPredicateRecipeDisplay(RecipeDisplay recipeDisplay, CallbackInfoReturnable<Boolean> cir) {
        int width = this.menu.getGridWidth();
        int height = this.menu.getGridHeight();
        if (recipeDisplay instanceof ComponentPredicateRecipeDisplay) {
            cir.cancel();
            cir.setReturnValue(width >= 3 && height >= 3);
        }
    }

    @Inject(method = "fillGhostRecipe", at = @At("TAIL"))
    private void fillComponentPredicateRecipeDisplay(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap, CallbackInfo ci) {
        if (recipeDisplay instanceof ComponentPredicateRecipeDisplay componentPredicateRecipeDisplay) {
            List<Slot> list = this.menu.getInputGridSlots();
            for (int index = 0; index < 9; index++) {
                Slot slot = list.get(index);
                SlotDisplay slotDisplay = componentPredicateRecipeDisplay.ingredients().get(index);
                ghostSlots.setInput(slot, contextMap, slotDisplay);
            }
        }
    }
}

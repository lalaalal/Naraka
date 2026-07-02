package com.yummy.naraka.mixin;

import com.yummy.naraka.world.item.crafting.NbtPredicateRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Iterator;

@Mixin(ServerPlaceRecipe.class)
public abstract class ServerPlaceRecipeMixin<C extends Container> implements PlaceRecipe<Integer> {
    @Shadow
    protected RecipeBookMenu<C> menu;

    @Shadow
    protected abstract void moveItemToGrid(Slot slotToFill, ItemStack ingredient);

    @Unique
    private Iterator<CompoundTag> naraka$ingredientTags = Collections.emptyIterator();


    @Inject(method = "handleRecipeClicked", at = @At("HEAD"))
    private void storeCurrentRecipe(Recipe<C> recipe, boolean placeAll, CallbackInfo ci) {
        if (recipe instanceof NbtPredicateRecipe nbtPredicateRecipe) {
            naraka$ingredientTags = nbtPredicateRecipe.getRecipeSlots().stream()
                    .map(NbtPredicateRecipe.RecipeSlot::tag)
                    .iterator();
        } else {
            naraka$ingredientTags = Collections.emptyIterator();
        }
    }

    @Override
    public void addItemToSlot(Iterator<Integer> ingredients, int slot, int maxAmount, int y, int x) {
        Slot slot2 = this.menu.getSlot(slot);
        ItemStack itemStack = StackedContents.fromStackingIndex(ingredients.next());
        if (naraka$ingredientTags.hasNext()) {
            CompoundTag compoundTag = naraka$ingredientTags.next();
            itemStack.setTag(compoundTag);
        }
        if (!itemStack.isEmpty()) {
            for (int i = 0; i < maxAmount; i++) {
                this.moveItemToGrid(slot2, itemStack);
            }
        }
    }
}

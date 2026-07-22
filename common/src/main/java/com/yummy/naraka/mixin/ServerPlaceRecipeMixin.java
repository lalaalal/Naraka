package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.world.item.crafting.NbtPredicateRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
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
import java.util.Objects;

@Mixin(ServerPlaceRecipe.class)
public abstract class ServerPlaceRecipeMixin<C extends Container> implements PlaceRecipe<Integer> {
    @Shadow
    protected Inventory inventory;
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

    @Inject(method = "addItemToSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    public void addItemToSlot(Iterator<Integer> ingredients, int slotIndex, int maxAmount, int y, int x, CallbackInfo ci, @Local ItemStack itemStack) {
        if (naraka$ingredientTags.hasNext()) {
            CompoundTag compoundTag = naraka$ingredientTags.next();
            itemStack.setTag(compoundTag);
        }
    }

    @ModifyExpressionValue(method = "moveItemToGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;findSlotMatchingUnusedItem(Lnet/minecraft/world/item/ItemStack;)I"))
    private int fixItemTagCompare(int original, @Local(argsOnly = true) ItemStack ingredient) {
        return naraka$findSlotForRecipe(ingredient);
    }

    @Unique
    private int naraka$findSlotForRecipe(ItemStack ingredient) {
        for (int index = 0; index < inventory.items.size(); index++) {
            ItemStack inventoryItem = inventory.items.get(index);
            if (!inventory.items.get(index).isEmpty()
                    && naraka$isSufficientForRequirement(ingredient, inventoryItem)
                    && !inventory.items.get(index).isDamaged()
                    && !inventoryItem.isEnchanted()
                    && !inventoryItem.hasCustomHoverName()) {
                return index;
            }
        }

        return -1;
    }

    @Unique
    private boolean naraka$isSufficientForRequirement(ItemStack ingredient, ItemStack compare) {
        if (ingredient.isEmpty() || compare.isEmpty())
            return false;
        if (!ingredient.is(compare.getItem()))
            return false;
        CompoundTag requirement = ingredient.getTag();
        CompoundTag compareTag = compare.getTag();
        if (requirement == null)
            return true;
        if (compareTag == null)
            return false;
        for (String key : requirement.getAllKeys()) {
            Tag value = compareTag.get(key);
            if (value == null || !Objects.equals(requirement.get(key), value))
                return false;
        }
        return true;
    }
}

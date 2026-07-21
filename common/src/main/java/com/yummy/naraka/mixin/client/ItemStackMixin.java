package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.event.ItemEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract boolean is(Item item);

    @Inject(method = "getTooltipLines", at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/ItemStack$TooltipPart;ADDITIONAL:Lnet/minecraft/world/item/ItemStack$TooltipPart;", opcode = Opcodes.GETSTATIC))
    public void addTooltipToTop(@Nullable Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
        if (player != null)
            ItemEvents.ITEM_TOOLTIP_TOP.invoker().addToTooltip(naraka$self(), player, isAdvanced, list::add);
    }

    @Inject(method = "getTooltipLines", at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/ItemStack$TooltipPart;UNBREAKABLE:Lnet/minecraft/world/item/ItemStack$TooltipPart;", opcode = Opcodes.GETSTATIC))
    public void addTooltipToBottom(@Nullable Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
        if (player != null)
            ItemEvents.ITEM_TOOLTIP_BOTTOM.invoker().addToTooltip(naraka$self(), player, isAdvanced, list::add);
    }

    @Unique
    private ItemStack naraka$self() {
        return (ItemStack) (Object) this;
    }
}

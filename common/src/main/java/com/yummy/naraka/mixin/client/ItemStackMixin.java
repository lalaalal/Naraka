package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.yummy.naraka.event.ItemEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V"))
    public void addTooltipToTop(@Nullable Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
        if (player != null)
            ItemEvents.ITEM_TOOLTIP_TOP.invoker().addToTooltip(naraka$self(), player, isAdvanced, naraka$hasShiftDown(), list::add);
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V", shift = At.Shift.AFTER))
    public void addTooltipToMiddle(@Nullable Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
        if (player != null)
            ItemEvents.ITEM_TOOLTIP_MIDDLE.invoker().addToTooltip(naraka$self(), player, isAdvanced, naraka$hasShiftDown(), list::add);
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    public void addTooltipToBottom(@Nullable Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> components = cir.getReturnValue();
        if (player != null)
            ItemEvents.ITEM_TOOLTIP_BOTTOM.invoker().addToTooltip(naraka$self(), player, isAdvanced, naraka$hasShiftDown(), components::add);
    }

    @Unique
    private ItemStack naraka$self() {
        return (ItemStack) (Object) this;
    }

    @Unique
    private boolean naraka$hasShiftDown() {
        Window window = Minecraft.getInstance().getWindow();
        return InputConstants.isKeyDown(window.getWindow(), InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(window.getWindow(), InputConstants.KEY_RSHIFT);
    }
}

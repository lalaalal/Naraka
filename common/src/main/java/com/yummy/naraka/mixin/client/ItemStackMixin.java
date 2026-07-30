package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.yummy.naraka.event.ItemEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {
    @Inject(method = "getTooltipLines", at = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;HIDE_ADDITIONAL_TOOLTIP:Lnet/minecraft/core/component/DataComponentType;", opcode = Opcodes.GETSTATIC))
    public void addTopTooltip(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> components) {
        if (player != null)
            ItemEvents.ITEM_TOOLTIP_TOP.invoker().addToTooltip(naraka$self(), tooltipContext, player, tooltipFlag, naraka$hasShiftDown(), components::add);
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V", shift = At.Shift.AFTER))
    public void addMiddleTooltip(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> components) {
        if (player != null)
            ItemEvents.ITEM_TOOLTIP_MIDDLE.invoker().addToTooltip(naraka$self(), tooltipContext, player, tooltipFlag, naraka$hasShiftDown(), components::add);
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    public void addBottomTooltip(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> components = cir.getReturnValue();
        if (player != null)
            ItemEvents.ITEM_TOOLTIP_BOTTOM.invoker().addToTooltip(naraka$self(), tooltipContext, player, tooltipFlag, naraka$hasShiftDown(), components::add);
    }

    @Unique
    private boolean naraka$hasShiftDown() {
        Window window = Minecraft.getInstance().getWindow();
        return InputConstants.isKeyDown(window.getWindow(), InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(window.getWindow(), InputConstants.KEY_RSHIFT);
    }

    @Unique
    private ItemStack naraka$self() {
        return (ItemStack) (Object) this;
    }
}

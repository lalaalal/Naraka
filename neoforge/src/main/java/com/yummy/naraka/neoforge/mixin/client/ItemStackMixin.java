package com.yummy.naraka.neoforge.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {
    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/AttributeUtil;addAttributeTooltips(Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;Lnet/neoforged/neoforge/common/util/AttributeTooltipContext;)V", shift = At.Shift.AFTER))
    public void addBlessedTooltip(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
        if (getOrDefault(NarakaDataComponentTypes.BLESSED, false))
            list.add(Component.translatable(LanguageKey.BLESSED_KEY)
                    .withStyle(ComponentStyles.LONGINUS_COLOR));
    }
}
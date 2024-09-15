package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.data.lang.NarakaLanguageProviders;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {
    @Unique
    private List<Component> lines = new ArrayList<>();

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;empty()Lnet/minecraft/network/chat/MutableComponent;"))
    public void storeLines(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
        this.lines = list;
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", ordinal = 0))
    public void addReinforcementTooltip(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        TooltipProvider tooltipProvider = Reinforcement.get(this);
        tooltipProvider.addToTooltip(tooltipContext, lines::add, tooltipFlag);
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addAttributeTooltips(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/player/Player;)V", shift = At.Shift.AFTER))
    public void addBlessedTooltip(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        if (getOrDefault(NarakaDataComponentTypes.BLESSED, false))
            lines.add(Component.translatable(NarakaLanguageProviders.BLESSED_KEY)
                    .withStyle(ChatFormatting.BLUE));
    }
}

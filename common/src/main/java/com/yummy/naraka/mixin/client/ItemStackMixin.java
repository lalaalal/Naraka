package com.yummy.naraka.mixin.client;

import com.mojang.serialization.Codec;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "getTooltipLines", at = @At(value = "RETURN"))
    public void addBlessedTooltip(Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> components = cir.getReturnValue();
        Reinforcement reinforcement = Reinforcement.get(naraka$self(), player.level().registryAccess());
        reinforcement.addToTooltip(components::add);

        if (NarakaItemUtils.readNbtDataOrDefault(naraka$self(), "Blessed", Codec.BOOL, false))
            components.add(Component.translatable(LanguageKey.BLESSED_KEY).withStyle(ComponentStyles.RAINBOW_COLOR));
        if (NarakaItemUtils.readNbtDataOrDefault(naraka$self(), "HerobrineScarf", Codec.BOOL, false))
            components.add(Component.translatable(LanguageKey.HEROBRINE_SCARF_KEY).withStyle(ComponentStyles.RAINBOW_COLOR));
    }

    @Unique
    private ItemStack naraka$self() {
        return (ItemStack) (Object) this;
    }
}

package com.yummy.naraka.mixin;

import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    public void preventSoulInfusedArmorEnchanting(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(NarakaItemTags.PURIFIED_SOUL_ARMOR)) {
            Reinforcement reinforcement = Reinforcement.get(stack);
            if (reinforcement.value() == 0) {
                cir.cancel();
                cir.setReturnValue(false);
            }
        }
    }
}

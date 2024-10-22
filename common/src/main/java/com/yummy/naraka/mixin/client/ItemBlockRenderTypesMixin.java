package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.CustomItemRenderManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemBlockRenderTypes.class)
public abstract class ItemBlockRenderTypesMixin {
    @Inject(method = "getRenderType(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/client/renderer/RenderType;", at = @At("HEAD"), cancellable = true)
    private static void getCustomRenderType(ItemStack itemStack, boolean bl, CallbackInfoReturnable<RenderType> cir) {
        if (CustomItemRenderManager.hasCustomRenderType(itemStack)) {
            cir.cancel();
            cir.setReturnValue(CustomItemRenderManager.getCustomRenderType(itemStack));
        }
    }
}

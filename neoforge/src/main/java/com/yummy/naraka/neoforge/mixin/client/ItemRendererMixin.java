package com.yummy.naraka.neoforge.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getFoilBuffer(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/RenderType;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
    )
    public RenderType modifyFoilBufferRenderType(RenderType original, @Local(argsOnly = true) ItemStack itemStack) {
        if (CustomRenderManager.hasCustomRenderType(itemStack))
            return CustomRenderManager.getCustomRenderType(itemStack, original);
        return original;
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getFoilBufferDirect(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/RenderType;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
    )
    public RenderType modifyFoilBufferDirectRenderType(RenderType original, @Local(argsOnly = true) ItemStack itemStack) {
        if (CustomRenderManager.hasCustomRenderType(itemStack))
            return CustomRenderManager.getCustomRenderType(itemStack, original);
        return original;
    }
}

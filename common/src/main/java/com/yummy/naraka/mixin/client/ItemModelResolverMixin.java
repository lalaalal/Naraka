package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.ColoredItemRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemModelResolver.class)
public abstract class ItemModelResolverMixin {
    @Inject(method = "updateForTopItem", at = @At("HEAD"))
    public void storeItemRenderContext(ItemStackRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, boolean leftHand, Level level, LivingEntity entity, int seed, CallbackInfo ci) {
        ColoredItemRenderer.setCurrentRenderingItem(stack);
    }
}

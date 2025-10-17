package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.ItemColorRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
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
    public void storeItemRenderContext(ItemStackRenderState itemStackRenderState, ItemStack itemStack, ItemDisplayContext itemDisplayContext, Level level, ItemOwner itemOwner, int i, CallbackInfo ci) {
        if (itemStackRenderState instanceof ItemColorSetter itemColorSetter) {
            if (ItemColorRegistry.hasColorOverride(itemStack)) {
                itemColorSetter.naraka$setColor(ItemColorRegistry.getColor(itemStack));
            } else if (ItemColorRegistry.hasTemporaryColor(itemStackRenderState)) {
                itemColorSetter.naraka$setColor(ItemColorRegistry.getTemporaryColor(itemStackRenderState));
            }
        }
    }
}

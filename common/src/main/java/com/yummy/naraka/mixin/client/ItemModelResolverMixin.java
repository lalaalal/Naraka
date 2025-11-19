package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.ItemColorSetter;
import com.yummy.naraka.client.renderer.ItemRenderRegistry;
import com.yummy.naraka.client.renderer.ItemRenderTypeSetter;
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
    @Inject(method = "updateForTopItem", at = @At("RETURN"))
    public void storeItemRenderContext(ItemStackRenderState itemStackRenderState, ItemStack itemStack, ItemDisplayContext itemDisplayContext, Level level, ItemOwner itemOwner, int i, CallbackInfo ci) {
        if (itemStackRenderState instanceof ItemColorSetter itemColorSetter) {
            if (ItemRenderRegistry.hasColorOverride(itemStack)) {
                itemColorSetter.naraka$setColor(ItemRenderRegistry.getColor(itemStack));
            } else if (ItemRenderRegistry.hasTemporaryColor(itemStackRenderState)) {
                itemColorSetter.naraka$setColor(ItemRenderRegistry.getTemporaryColor(itemStackRenderState));
            }
        }

        if (itemStackRenderState instanceof ItemRenderTypeSetter itemRenderTypeSetter) {
            if (ItemRenderRegistry.hasRenderTypeOverride(itemStack)) {
                ItemRenderRegistry.setRenderType(itemStack, itemRenderTypeSetter);
            }
        }
    }
}

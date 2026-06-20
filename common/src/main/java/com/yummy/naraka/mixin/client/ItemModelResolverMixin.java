package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.ItemColorSetter;
import com.yummy.naraka.client.renderer.ItemRenderRegistry;
import com.yummy.naraka.client.renderer.ItemRenderTypeSetter;
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

@Mixin(ItemModelResolver.class)
public abstract class ItemModelResolverMixin {
    @Inject(method = "updateForTopItem", at = @At("RETURN"))
    public void storeItemRenderContext(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Level level, ItemOwner owner, int seed, CallbackInfo ci) {
        if (output instanceof ItemColorSetter itemColorSetter) {
            if (ItemRenderRegistry.hasColorOverride(item)) {
                itemColorSetter.naraka$setColor(ItemRenderRegistry.getColor(item));
            } else if (ItemRenderRegistry.hasTemporaryColor(output)) {
                itemColorSetter.naraka$setColor(ItemRenderRegistry.getTemporaryColor(output));
            }
        }

        if (output instanceof ItemRenderTypeSetter itemRenderTypeSetter) {
            if (ItemRenderRegistry.hasRenderTypeOverride(item)) {
                ItemRenderRegistry.setRenderType(item, itemRenderTypeSetter);
            }
        }
    }
}

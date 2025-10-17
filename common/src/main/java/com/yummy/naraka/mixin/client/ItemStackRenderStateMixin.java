package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemStackRenderState.class)
public abstract class ItemStackRenderStateMixin implements ItemColorSetter {
    @Shadow
    private ItemStackRenderState.LayerRenderState[] layers;
    @Shadow
    ItemDisplayContext displayContext;
    @Unique
    private int naraka$color = -1;

    @Override
    public void naraka$setColor(int color) {
        this.naraka$color = color;
    }

    @Inject(method = "submit", at = @At("HEAD"))
    private void setLayerColors(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, int k, CallbackInfo ci) {
        for (ItemStackRenderState.LayerRenderState layer : layers) {
            if (layer instanceof LayerRenderStateSetter layerRenderStateSetter) {
                layerRenderStateSetter.naraka$setColor(naraka$color);
                layerRenderStateSetter.naraka$setItemDisplayContext(displayContext);
            }
        }
    }

    @Inject(method = "isAnimated", at = @At("HEAD"), cancellable = true)
    private void alwaysRenderColoredItem(CallbackInfoReturnable<Boolean> cir) {
        if (naraka$color != -1) {
            cir.cancel();
            cir.setReturnValue(true);
        }
    }
}

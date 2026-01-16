package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.ItemColorSetter;
import com.yummy.naraka.client.renderer.ItemRenderTypeSetter;
import com.yummy.naraka.client.renderer.LayerRenderStateSetter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemStackRenderState.class)
public abstract class ItemStackRenderStateMixin implements ItemColorSetter, ItemRenderTypeSetter {
    @Shadow
    private ItemStackRenderState.LayerRenderState[] layers;
    @Shadow
    ItemDisplayContext displayContext;
    @Shadow private boolean animated;
    @Unique
    private int naraka$color = -1;

    @Override
    public void naraka$setColor(int color) {
        this.naraka$color = color;
        this.animated = true;
    }

    @Override
    public void naraka$setRenderType(RenderType renderType) {
        for (ItemStackRenderState.LayerRenderState layer : layers)
            layer.setRenderType(renderType);
        this.animated = true;
    }

    @Override
    public void naraka$setRenderType(RenderType renderType, int layer) {
        layers[layer].setRenderType(renderType);
        this.animated = true;
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
}

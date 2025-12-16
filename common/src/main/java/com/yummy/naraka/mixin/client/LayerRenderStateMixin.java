package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.ColoredItemSubmitNodeCollection;
import com.yummy.naraka.client.renderer.LayerRenderStateSetter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemStackRenderState.LayerRenderState.class)
public abstract class LayerRenderStateMixin implements LayerRenderStateSetter {
    @Shadow
    private int[] tintLayers;
    @Shadow @Final
    private List<BakedQuad> quads;
    @Shadow @Nullable
    private RenderType renderType;
    @Shadow
    private ItemStackRenderState.FoilType foilType;

    @Unique
    private int naraka$color = -1;
    @Unique
    private ItemDisplayContext naraka$itemDisplayContext = ItemDisplayContext.NONE;

    @Override
    public void naraka$setColor(int color) {
        this.naraka$color = color;
    }

    @Override
    public void naraka$setItemDisplayContext(ItemDisplayContext itemDisplayContext) {
        this.naraka$itemDisplayContext = itemDisplayContext;
    }

    @Inject(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"), cancellable = true)
    public void submitColoredItem(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, int k, CallbackInfo ci) {
        if (renderType != null && naraka$color != -1) {
            ci.cancel();
            if (submitNodeCollector.order(0) instanceof ColoredItemSubmitNodeCollection coloredItemSubmitNodeCollection)
                coloredItemSubmitNodeCollection.naraka$submitColoredItem(poseStack, naraka$itemDisplayContext, i, j, naraka$color, k, this.tintLayers, this.quads, this.renderType, this.foilType);
            poseStack.popPose();
        }
    }
}

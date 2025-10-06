package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.ColoredItemSubmitNodeCollector;
import com.yummy.naraka.client.renderer.ColoredItemSubmitNodeProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin implements ColoredItemSubmitNodeCollector, ColoredItemSubmitNodeProvider {
    @Shadow
    private boolean wasUsed;
    @Unique
    private final Map<SubmitNodeStorage.ItemSubmit, Integer> naraka$coloredItemSubmits = new HashMap<>();

    @Override
    public void naraka$submitColoredItem(PoseStack poseStack, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int color, int outlineColor, int[] tintLayers, List<BakedQuad> quads, RenderType renderType, ItemStackRenderState.FoilType foilType) {
        wasUsed = true;
        this.naraka$coloredItemSubmits.put(new SubmitNodeStorage.ItemSubmit(poseStack.last().copy(), displayContext, lightCoords, overlayCoords, outlineColor, tintLayers, quads, renderType, foilType), color);
    }

    @Override
    public Map<SubmitNodeStorage.ItemSubmit, Integer> naraka$getColoredItemSubmits() {
        return naraka$coloredItemSubmits;
    }

    @Inject(method = "clear", at = @At("TAIL"))
    public void clearColoredItemSubmits(CallbackInfo ci) {
        naraka$coloredItemSubmits.clear();
    }
}

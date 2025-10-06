package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.client.renderer.ColoredItemFeatureRenderer;
import com.yummy.naraka.client.renderer.ColoredItemSubmitNodeProvider;
import com.yummy.naraka.client.renderer.ItemColorRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderDispatcher.class)
public abstract class FeatureRenderDispatcherMixin {
    @Shadow @Final
    private MultiBufferSource.BufferSource bufferSource;
    @Shadow @Final
    private OutlineBufferSource outlineBufferSource;
    @Unique
    private final ColoredItemFeatureRenderer naraka$coloredItemFeatureRenderer = new ColoredItemFeatureRenderer();

    @Inject(method = "renderAllFeatures", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer;render(Lnet/minecraft/client/renderer/SubmitNodeCollection;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/OutlineBufferSource;)V"))
    private void renderColoredItemFeature(CallbackInfo ci, @Local SubmitNodeCollection submitNodeCollection) {
        if (submitNodeCollection instanceof ColoredItemSubmitNodeProvider coloredItemSubmitNodeProvider)
            this.naraka$coloredItemFeatureRenderer.render(coloredItemSubmitNodeProvider, this.bufferSource, this.outlineBufferSource);
    }

    @Inject(method = "renderAllFeatures", at = @At(value = "TAIL"))
    private void resetTemporaryColors(CallbackInfo ci) {
        ItemColorRegistry.clearTemporary();
    }
}

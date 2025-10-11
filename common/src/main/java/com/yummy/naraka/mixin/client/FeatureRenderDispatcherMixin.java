package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.client.renderer.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.resources.model.AtlasManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(FeatureRenderDispatcher.class)
public abstract class FeatureRenderDispatcherMixin {
    @Shadow @Final
    private MultiBufferSource.BufferSource bufferSource;
    @Shadow @Final
    private OutlineBufferSource outlineBufferSource;
    @Shadow
    @Final
    private AtlasManager atlasManager;
    @Unique
    private final ColoredItemFeatureRenderer naraka$coloredItemFeatureRenderer = new ColoredItemFeatureRenderer();
    @Unique
    private final PurifiedSoulFlameFeatureRenderer naraka$purifiedSoulFlameFeatureRenderer = new PurifiedSoulFlameFeatureRenderer();

    @Inject(method = "renderAllFeatures", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer;render(Lnet/minecraft/client/renderer/SubmitNodeCollection;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/OutlineBufferSource;)V"))
    private void renderColoredItemFeature(CallbackInfo ci, @Local SubmitNodeCollection submitNodeCollection) {
        if (submitNodeCollection instanceof ColoredItemSubmitNodeCollection coloredItemSubmitNodeCollection)
            this.naraka$coloredItemFeatureRenderer.render(coloredItemSubmitNodeCollection, this.bufferSource, this.outlineBufferSource);
        if (submitNodeCollection instanceof PurifiedSoulFireSubmitNodeCollection purifiedSoulFireSubmitNodeCollection)
            this.naraka$purifiedSoulFlameFeatureRenderer.render(purifiedSoulFireSubmitNodeCollection, this.bufferSource, this.atlasManager);
    }

    @Inject(method = "renderAllFeatures", at = @At(value = "TAIL"))
    private void resetTemporaryColors(CallbackInfo ci) {
        ItemColorRegistry.clearTemporary();
    }
}

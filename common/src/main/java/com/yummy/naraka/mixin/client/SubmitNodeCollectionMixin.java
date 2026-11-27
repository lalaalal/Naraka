package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.renderer.RyoikiMaskExtension;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.CustomFeatureRenderer;
import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
import net.minecraft.client.renderer.feature.phase.TranslucentFeatureRenderPhase;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin implements RyoikiMaskExtension<SimpleFeatureRenderPhase> {
    @Shadow
    @Mutable
    @Final
    private List<FeatureRenderPhase<?>> allPhases;

    @Unique
    private final SimpleFeatureRenderPhase naraka$ryoikiMask = new SimpleFeatureRenderPhase();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void includeRyoikiMask(boolean useImprovedTransparency, TranslucentFeatureRenderPhase seeThrough, CallbackInfo ci) {
        List<FeatureRenderPhase<?>> extended = new ArrayList<>(allPhases);
        extended.add(naraka$ryoikiMask);
        allPhases = Collections.unmodifiableList(extended);
    }

    @Override
    public SimpleFeatureRenderPhase naraka$getRyoikiMask() {
        return naraka$ryoikiMask;
    }

    @Override
    public void naraka$setRyoikiMask(SimpleFeatureRenderPhase ryoikiMask) {
        throw new UnsupportedOperationException();
    }

    @Inject(method = "submitCustomGeometry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderType;isOutline()Z"), cancellable = true)
    private void checkRyoikiMask(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer, CallbackInfo ci, @Local(name = "submit") CustomFeatureRenderer.Submit submit) {
        if (renderType == NarakaRenderTypes.ryoikiMask()) {
            naraka$ryoikiMask.submit(submit);
            ci.cancel();
        }
    }
}

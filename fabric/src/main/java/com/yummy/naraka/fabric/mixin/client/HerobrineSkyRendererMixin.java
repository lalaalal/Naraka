package com.yummy.naraka.fabric.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.yummy.naraka.client.renderer.HerobrineSkyRenderHelper;
import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class HerobrineSkyRendererMixin {
    @Shadow @Final
    private SkyRenderer skyRenderer;
    @Shadow @Final
    private RenderBuffers renderBuffers;

    @Inject(method = "addSkyPass", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V"), cancellable = true)
    public void replaceHerobrineSkyPass(FrameGraphBuilder frameGraphBuilder, Camera camera, float partialTick, FogParameters fog, CallbackInfo ci, @Local DimensionSpecialEffects.SkyType skyType, @Local FramePass framePass) {
        if (skyType == DimensionSpecialEffects.SkyType.OVERWORLD && NarakaConfig.CLIENT.renderHerobrineSky.getValue()) {
            ci.cancel();
            framePass.executes(() -> HerobrineSkyRenderHelper.renderHerobrineSky(skyRenderer, renderBuffers, fog));
        }
    }
}

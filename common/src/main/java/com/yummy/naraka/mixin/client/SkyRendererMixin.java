package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.renderer.HerobrineSkyRenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {
    @Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
    public void replaceMoon(int phase, float alpha, MultiBufferSource bufferSource, PoseStack poseStack, CallbackInfo ci) {
        if (NarakaClientContext.ENABLE_HEROBRINE_SKY.getValue() && NarakaClientContext.SHADER_ENABLED.getValue()) {
            ci.cancel();
            HerobrineSkyRenderHelper.renderEclipse(poseStack, bufferSource);
        }
    }
}

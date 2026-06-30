package com.yummy.naraka.fabric.mixin.client;

import com.yummy.naraka.client.NarakaClientContext;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @ModifyArg(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;F)V"))
    private float fixParticlePartialTickOnTickFreeze(float partialTicks) {
        if (NarakaClientContext.TICK_FROZEN.getValue())
            return NarakaClientContext.FROZEN_PARTIAL_TICK.getValue();
        return partialTicks;
    }
}

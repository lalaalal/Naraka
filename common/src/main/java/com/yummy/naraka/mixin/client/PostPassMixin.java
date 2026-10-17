package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.RenderPass;
import com.yummy.naraka.client.renderer.PostEffectUniformBuffer;
import com.yummy.naraka.client.renderer.PostEffectUniformExtension;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(PostPass.class)
public abstract class PostPassMixin implements PostEffectUniformExtension {
    @Unique
    @Nullable
    private Identifier naraka$postEffectId;

    @Inject(method = "lambda$addToFrame$1", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;draw(II)V"))
    private void addCustomUniforms(ResourceHandle<RenderTarget> outputHandle, GpuBufferSlice shaderOrthoMatrix, Map<Identifier, ResourceHandle<RenderTarget>> targets, CallbackInfo ci, @Local(name = "renderPass") RenderPass renderPass) {
        if (naraka$postEffectId != null)
            PostEffectUniformBuffer.getInstance().setUniform(naraka$postEffectId, renderPass);
    }

    @Override
    public void naraka$setPostEffectId(Identifier postEffectId) {
        this.naraka$postEffectId = postEffectId;
    }
}

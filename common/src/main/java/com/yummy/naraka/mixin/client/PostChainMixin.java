package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.renderer.PostEffectUniformBuffer;
import com.yummy.naraka.client.renderer.PostEffectUniformExtension;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostChainConfig;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PostChain.class)
public abstract class PostChainMixin {
    @Unique
    private Identifier naraka$postEffectId = NarakaMod.identifier("empty");

    @ModifyReturnValue(method = "load", at = @At("RETURN"))
    private static PostChain setPostEffectId(PostChain original, @Local(argsOnly = true, name = "id") Identifier postEffectId) {
        ((PostChainMixin) (Object) original).naraka$postEffectId = postEffectId;
        return original;
    }

    @Inject(method = "createPass", at = @At(value = "INVOKE", target = "Ljava/util/Map;keySet()Ljava/util/Set;"))
    private static void definePostEffectUniforms(TextureManager textureManager, PostChainConfig.Pass config, Identifier id, CallbackInfoReturnable<PostPass> cir, @Local(name = "pipelineBuilder") RenderPipeline.Builder pipelineBuilder) {
        PostEffectUniformBuffer.getInstance().defineUniforms(id, pipelineBuilder);
    }

    @Inject(method = "addToFrame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostPass;addToFrame(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Ljava/util/Map;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"))
    private void prepareUniforms(FrameGraphBuilder frame, int screenWidth, int screenHeight, PostChain.TargetBundle providedTargets, CallbackInfo ci, @Local(name = "pass") PostPass pass) {
        if (pass instanceof PostEffectUniformExtension extension) {
            extension.naraka$setPostEffectId(this.naraka$postEffectId);
        }
    }
}

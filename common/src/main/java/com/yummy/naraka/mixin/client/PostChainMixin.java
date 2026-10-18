package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.yummy.naraka.client.renderer.uniform.PostEffectUniformExtension;
import com.yummy.naraka.client.renderer.uniform.UniformLayoutRegistry;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostChainConfig;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PostChain.class)
public abstract class PostChainMixin {
    @Shadow
    @Final
    private Identifier id;

    @Inject(method = "createPass", at = @At(value = "INVOKE", target = "Ljava/util/Map;keySet()Ljava/util/Set;"))
    private static void definePostEffectUniforms(TextureManager textureManager, PostChainConfig.Pass config, Identifier id, CallbackInfoReturnable<PostPass> cir, @Local(name = "bindGroupLayoutBuilder") BindGroupLayout.Builder bindGroupLayoutBuilder) {
        UniformLayoutRegistry.getInstance().defineUniform(id, bindGroupLayoutBuilder);
    }

    @Inject(method = "addToFrame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostPass;addToFrame(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Ljava/util/Map;Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;)V"))
    private void prepareUniforms(FrameGraphBuilder frame, int screenWidth, int screenHeight, PostChain.TargetBundle providedTargets, CallbackInfo ci, @Local(name = "pass") PostPass pass) {
        if (pass instanceof PostEffectUniformExtension extension) {
            extension.naraka$setPostEffectId(this.id);
        }
    }
}

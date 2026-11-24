package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.yummy.naraka.client.MixinClientContext;
import com.yummy.naraka.client.renderer.uniform.PostEffectUniformExtension;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PostChain.class)
public abstract class PostChainMixin {
    @Inject(method = "addPass", at = @At("HEAD"))
    public void prepare(String name, RenderTarget inTarget, RenderTarget outTarget, boolean useLinearFilter, CallbackInfoReturnable<PostPass> cir) {
        MixinClientContext.DYNAMIC_POST_EFFECT.set(ResourceLocation.parse(name));
    }

    @Inject(method = "addPass", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V"))
    public void setDynamicPostEffectId(String name, RenderTarget inTarget, RenderTarget outTarget, boolean useLinearFilter, CallbackInfoReturnable<PostPass> cir, @Local PostPass postPass) {
        ((PostEffectUniformExtension) postPass).naraka$setPostEffectId(ResourceLocation.parse(name));
        MixinClientContext.DYNAMIC_POST_EFFECT.remove();
    }
}

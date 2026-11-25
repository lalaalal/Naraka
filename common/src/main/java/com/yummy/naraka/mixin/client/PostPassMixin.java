package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.uniform.PostEffectUniformExtension;
import com.yummy.naraka.client.renderer.uniform.UniformLayoutRegistry;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(PostPass.class)
public abstract class PostPassMixin implements PostEffectUniformExtension {
    @Shadow
    @Final
    private EffectInstance effect;
    @Unique
    @Nullable
    ResourceLocation naraka$dynamicPostEffect;

    @Override
    public void naraka$setPostEffectId(ResourceLocation dynamicPostEffect) {
        this.naraka$dynamicPostEffect = dynamicPostEffect;
    }

    @Inject(method = "process", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EffectInstance;apply()V"))
    public void setDynamicPostEffectUniforms(float partialTicks, CallbackInfo ci) {
        if (naraka$dynamicPostEffect != null) {
            UniformLayoutRegistry.getInstance().setUniform(naraka$dynamicPostEffect, effect);
        }
    }
}

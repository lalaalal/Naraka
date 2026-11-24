package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.shaders.Uniform;
import com.yummy.naraka.client.MixinClientContext;
import com.yummy.naraka.client.renderer.uniform.UniformLayoutRegistry;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EffectInstance.class)
public abstract class EffectInstanceMixin {
    @Shadow
    @Final
    private List<Uniform> uniforms;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EffectInstance;parseBlendNode(Lcom/google/gson/JsonObject;)Lcom/mojang/blaze3d/shaders/BlendMode;"))
    public void addDynamicPostEffectUniforms(ResourceProvider resourceProvider, String name, CallbackInfo ci) {
        ResourceLocation dynamicPostEffect = MixinClientContext.DYNAMIC_POST_EFFECT.get();
        if (dynamicPostEffect != null)
            UniformLayoutRegistry.getInstance().defineUniform(dynamicPostEffect, naraka$self(), this.uniforms::add);
    }

    @Unique
    private EffectInstance naraka$self() {
        return (EffectInstance) (Object) this;
    }
}

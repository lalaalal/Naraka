package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.NarakaClientContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Unique @Nullable
    private PostChain naraka$previousPostEffect;
    @Unique
    private boolean naraka$previousEffectActive;
    @Unique
    private boolean naraka$monochromeEffectActive = false;

    @Shadow private boolean effectActive;

    @Shadow @Nullable
    PostChain postEffect;

    @Shadow
    protected abstract void loadEffect(ResourceLocation resourceLocation);

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void checkMonochromePostEffect(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (NarakaClientContext.POST_EFFECT_TICK.getValue() > 0 && !naraka$monochromeEffectActive) {
            naraka$previousPostEffect = postEffect;
            naraka$previousEffectActive = effectActive;
            loadEffect(NarakaClientContext.POST_EFFECT.getValue());
            naraka$monochromeEffectActive = true;
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"))
    private void restorePostEffect(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (NarakaClientContext.POST_EFFECT_TICK.getValue() == 0 && naraka$monochromeEffectActive) {
            if (postEffect != null)
                postEffect.close();
            postEffect = naraka$previousPostEffect;
            effectActive = naraka$previousEffectActive;
            naraka$monochromeEffectActive = false;
        }
    }
}

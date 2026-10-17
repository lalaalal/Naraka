package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.renderer.PostEffectUniformBuffer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Unique
    private final PostEffectUniformBuffer postEffectUniformBuffer = new PostEffectUniformBuffer();
    @Shadow
    @Final
    private GameRenderState gameRenderState;
    @Unique
    @Nullable
    private Identifier naraka$previousPostEffect;
    @Unique
    private boolean naraka$previousEffectActive;

    @Shadow
    @Nullable
    private Identifier postEffectId;

    @Shadow
    private boolean effectActive;

    @Shadow
    protected abstract void setPostEffect(Identifier id);

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void checkMonochromePostEffect(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        naraka$previousPostEffect = postEffectId;
        naraka$previousEffectActive = effectActive;
        if (NarakaClientContext.POST_EFFECT_TICK.getValue() > 0) {
            Identifier newPostEffectId = NarakaClientContext.POST_EFFECT.getValue();
            setPostEffect(newPostEffectId);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/FogRenderer;endFrame()V"))
    private void restorePostEffect(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        postEffectId = naraka$previousPostEffect;
        effectActive = naraka$previousEffectActive;
    }

    @Inject(method = "extract", at = @At("RETURN"))
    public void extractPostEffectUniform(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        if (NarakaClientContext.POST_EFFECT_TICK.getValue() > 0) {
            Identifier newPostEffectId = NarakaClientContext.POST_EFFECT.getValue();
            float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
            postEffectUniformBuffer.update(gameRenderState.levelRenderState.gameTime + partialTicks, newPostEffectId);
        }
    }

    @Inject(method = "close", at = @At("RETURN"))
    public void closePostEffectUniformBuffer(CallbackInfo ci) {
        postEffectUniformBuffer.close();
    }
}

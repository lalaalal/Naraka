package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.UniformLayoutRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private GameRenderState gameRenderState;
    @Unique
    private final UniformLayoutRegistry uniformLayoutRegistry = new UniformLayoutRegistry();

    @Inject(method = "extract", at = @At("RETURN"))
    private void extractPostEffectUniform(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
        uniformLayoutRegistry.extract(gameRenderState.levelRenderState.gameTime + partialTicks, gameRenderState.requestedPostEffects);
    }

    @Inject(method = "close", at = @At("RETURN"))
    public void closePostEffectUniformLayout(CallbackInfo ci) {
        uniformLayoutRegistry.close();
    }
}

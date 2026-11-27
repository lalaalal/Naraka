package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.commands.RenderPassDescriptor;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.yummy.naraka.client.NarakaRenderPipelines;
import com.yummy.naraka.client.renderer.RyoikiMaskExtension;
import com.yummy.naraka.client.renderer.uniform.UniformLayoutRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
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

    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private RenderTarget hud3DTarget;
    @Unique
    private final UniformLayoutRegistry naraka$uniformLayoutRegistry = new UniformLayoutRegistry();

    @Inject(method = "extract", at = @At("RETURN"))
    private void extractPostEffectUniform(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        AvatarRenderState avatarRenderState = gameRenderState.levelRenderState.playerRenderState.avatarRenderState;
        if (avatarRenderState != null)
            naraka$uniformLayoutRegistry.extract(avatarRenderState.ageInTicks, gameRenderState.requestedPostEffects);
    }

    @Inject(method = "close", at = @At("RETURN"))
    public void onClose(CallbackInfo ci) {
        naraka$uniformLayoutRegistry.close();
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "integrate3DHudDepth", at = @At("RETURN"))
    private void clearHandMask(CallbackInfo ci) {
        RenderTarget ryoikiRenderTarget = ((RyoikiMaskExtension<RenderTarget>) minecraft.levelRenderer).naraka$getRyoikiMask();
        if (ryoikiRenderTarget.getColorTextureView() == null)
            return;
        RenderPassDescriptor integrateDepthDescriptor = RenderPassDescriptor.builder(() -> "Clear hand from ryoiki mask")
                .withColorAttachment(ryoikiRenderTarget.getColorTextureView())
                .build();
        GpuSampler nearestSampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);

        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(integrateDepthDescriptor)) {
            renderPass.setUniform("InSampler", this.hud3DTarget.getDepthTextureView(), nearestSampler);
            renderPass.setPipeline(RenderSystem.getCompiledPipeline(NarakaRenderPipelines.CLEAR_HAND_MASK));
            renderPass.draw(3, 1, 0, 0);
        }
    }
}

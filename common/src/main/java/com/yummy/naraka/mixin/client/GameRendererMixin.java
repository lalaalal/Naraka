package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.yummy.naraka.client.renderer.uniform.UniformLayoutRegistry;
import com.yummy.naraka.world.entity.data.DynamicPostEffectHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private CrossFrameResourcePool resourcePool;

    @Unique
    private List<Identifier> naraka$dynamicPostEffects = List.of();

    @Unique
    private final UniformLayoutRegistry naraka$uniformLayoutRegistry = new UniformLayoutRegistry();

    @Shadow
    public abstract Camera getMainCamera();

    @SuppressWarnings("deprecation")
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void dynamicPostEffect(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        for (Identifier dynamicPostEffect : naraka$dynamicPostEffects) {
            PostChain postChain = this.minecraft.getShaderManager().getPostChain(dynamicPostEffect, LevelTargetBundle.MAIN_TARGETS);
            if (postChain != null) {
                postChain.process(this.minecraft.getMainRenderTarget(), this.resourcePool);
            }
        }
    }

    @Inject(method = "extract", at = @At("RETURN"))
    public void extractPostEffectUniform(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        Entity entity = getMainCamera().entity();
        if (entity instanceof Player player) {
            float ageInTicks = entity.tickCount + deltaTracker.getGameTimeDeltaPartialTick(true);
            naraka$dynamicPostEffects = DynamicPostEffectHelper.get(player);
            naraka$uniformLayoutRegistry.extract(ageInTicks, naraka$dynamicPostEffects);
        }
    }

    @Inject(method = "close", at = @At("RETURN"))
    public void closePostEffectUniformLayout(CallbackInfo ci) {
        naraka$uniformLayoutRegistry.close();
    }
}

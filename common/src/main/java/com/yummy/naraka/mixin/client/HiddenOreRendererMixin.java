package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.HiddenOreRenderState;
import com.yummy.naraka.client.renderer.HiddenOreRenderStateProvider;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class)
public abstract class HiddenOreRendererMixin {
    @Inject(method = "submitFeatures", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;submitBlockEntities(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/state/level/LevelRenderState;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V"))
    private void submitHiddenOreOutlines(LevelRenderState levelRenderState, SubmitNodeCollector submitNodeCollector, boolean renderOutline, CallbackInfo ci, @Local(name = "poseStack") PoseStack poseStack) {
        if (levelRenderState instanceof HiddenOreRenderStateProvider hiddenOreRenderStateProvider) {
            for (HiddenOreRenderState renderState : hiddenOreRenderStateProvider.naraka$getHiddenOreRenderStates()) {
                Vec3 position = new Vec3(renderState.pos)
                        .subtract(levelRenderState.cameraRenderState.pos);

                poseStack.pushPose();
                poseStack.scale(0.98f, 0.98f, 0.98f);
                poseStack.translate(position);
                renderState.blockModel.submit(poseStack, submitNodeCollector, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, renderState.color);
                poseStack.popPose();
            }
            hiddenOreRenderStateProvider.naraka$clearHiddenOreRenderStates();
        }
    }
}

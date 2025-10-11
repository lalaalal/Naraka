package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.PurifiedSoulFireSubmitNodeCollection;
import com.yummy.naraka.client.renderer.entity.state.PurifiedSoulFlameRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", shift = At.Shift.AFTER))
    private <S extends EntityRenderState> void submitNarakaFlame(S entityRenderState, CameraRenderState cameraRenderState, double x, double y, double z, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CallbackInfo ci) {
        if (entityRenderState instanceof PurifiedSoulFlameRenderState purifiedSoulFlameRenderState && purifiedSoulFlameRenderState.naraka$displayPurifiedSoulFlame()) {
            if (submitNodeCollector.order(0) instanceof PurifiedSoulFireSubmitNodeCollection purifiedSoulFireSubmitNodeCollection) {
                purifiedSoulFireSubmitNodeCollection.naraka$submitFlame(poseStack, entityRenderState, Mth.rotationAroundAxis(Mth.Y_AXIS, cameraRenderState.orientation, new Quaternionf()));
            }
        }
    }
}

package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaSprites;
import com.yummy.naraka.client.renderer.entity.state.PurifiedSoulFlameRenderState;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
    @Shadow
    @Final
    private SpriteGetter sprites;

    @Shadow
    private static void submitFire(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, TextureAtlasSprite sprite) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(method = "submit", at = @At("RETURN"))
    private void renderPurifiedSoulFIre(float partialTicks, SubmitNodeCollector submitNodeCollector, PlayerRenderState playerRenderState, CameraRenderState cameraRenderState, boolean hideGui, CallbackInfo ci, @Local(name = "poseStack") PoseStack poseStack) {
        AvatarRenderState avatarRenderState = playerRenderState.avatarRenderState;
        if (avatarRenderState == null)
            return;

        if (cameraRenderState.isFirstPerson && !avatarRenderState.isSpectator) {
            if (playerRenderState instanceof PurifiedSoulFlameRenderState purifiedSoulFlameRenderState && purifiedSoulFlameRenderState.naraka$displayPurifiedSoulFlame()) {
                submitFire(poseStack, submitNodeCollector, sprites.get(NarakaSprites.PURIFIED_SOUL_FIRE_1));
            }
        }
    }
}

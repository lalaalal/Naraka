package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaSprites;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
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
    private Minecraft minecraft;

    @Shadow
    @Final
    private SpriteGetter sprites;

    @Shadow
    private static void renderFire(PoseStack poseStack, MultiBufferSource bufferSource, TextureAtlasSprite sprite) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    @Final
    private MultiBufferSource bufferSource;

    @Inject(method = "renderScreenEffect", at = @At("RETURN"))
    private void renderPurifiedSoulFIre(boolean isFirstPerson, boolean isSleeping, float partialTicks, SubmitNodeCollector submitNodeCollector, boolean hideGui, CallbackInfo ci, @Local(name = "poseStack") PoseStack poseStack) {
        if (minecraft.player != null && !minecraft.player.isSpectator() && this.minecraft.options.getCameraType().isFirstPerson()
                && EntityDataHelper.getRawEntityData(minecraft.player, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get()) > 0) {
            renderFire(poseStack, this.bufferSource, sprites.get(NarakaSprites.PURIFIED_SOUL_FIRE_1));
        }
    }
}

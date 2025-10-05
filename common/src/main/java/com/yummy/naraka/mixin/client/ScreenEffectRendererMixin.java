package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.PurifiedSoulFireTextureProvider;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
    @Shadow
    private static void renderFire(PoseStack poseStack, MultiBufferSource bufferSource) {
        throw new AssertionError();
    }

    @ModifyExpressionValue(method = "renderFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    private static TextureAtlasSprite modifyFireSprite(TextureAtlasSprite original) {
        if (PurifiedSoulFireTextureProvider.isUsingPurifiedSoulFireTexture())
            return PurifiedSoulFireTextureProvider.modifyFire1();
        return original;
    }

    @Inject(method = "renderScreenEffect", at = @At("RETURN"))
    private static void renderPurifiedSoulFIre(Minecraft minecraft, PoseStack poseStack, MultiBufferSource bufferSource, CallbackInfo ci) {
        if (minecraft.player != null && !minecraft.player.isSpectator()
                && EntityDataHelper.getEntityData(minecraft.player, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get()) > 0) {
            PurifiedSoulFireTextureProvider.runWithPurifiedSoulFireTexture(() -> renderFire(poseStack, bufferSource));
        }
    }
}

package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.PurifiedSoulFlameFeatureRenderer;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.MaterialSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private MultiBufferSource bufferSource;

    @Shadow @Final private MaterialSet materials;

    @Shadow
    private static void renderFire(PoseStack poseStack, MultiBufferSource multiBufferSource, TextureAtlasSprite textureAtlasSprite) {
        throw new AssertionError();
    }

    @Inject(method = "renderScreenEffect", at = @At("RETURN"))
    private void renderPurifiedSoulFIre(boolean bl, float f, SubmitNodeCollector submitNodeCollector, CallbackInfo ci, @Local PoseStack poseStack) {
        if (minecraft.player != null && !minecraft.player.isSpectator() && this.minecraft.options.getCameraType().isFirstPerson()
                && EntityDataHelper.getRawEntityData(minecraft.player, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get()) > 0) {
            renderFire(poseStack, this.bufferSource, materials.get(PurifiedSoulFlameFeatureRenderer.PURIFIED_SOUL_FIRE_1));
        }
    }
}

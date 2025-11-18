package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.yummy.naraka.client.renderer.PurifiedSoulFlameRenderMaterial;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
    @Inject(method = "renderScreenEffect", at = @At("RETURN"))
    private static void renderPurifiedSoulFIre(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        if (minecraft.player != null && !minecraft.player.isSpectator() && minecraft.options.getCameraType().isFirstPerson()
                && EntityDataHelper.getRawEntityData(minecraft.player, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get()) > 0) {
            naraka$renderPurifiedSoulFire(minecraft, poseStack);
        }
    }

    @Unique
    private static void naraka$renderPurifiedSoulFire(Minecraft minecraft, PoseStack poseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        TextureAtlasSprite textureAtlasSprite = PurifiedSoulFlameRenderMaterial.PURIFIED_SOUL_FIRE_1.sprite();
        RenderSystem.setShaderTexture(0, textureAtlasSprite.atlasLocation());
        float u0 = textureAtlasSprite.getU0();
        float u1 = textureAtlasSprite.getU1();
        float uMiddle = (u0 + u1) / 2.0F;
        float v0 = textureAtlasSprite.getV0();
        float v1 = textureAtlasSprite.getV1();
        float vMiddle = (v0 + v1) / 2.0F;
        float ration = textureAtlasSprite.uvShrinkRatio();
        float highU = Mth.lerp(ration, u0, uMiddle);
        float lowU = Mth.lerp(ration, u1, uMiddle);
        float highV = Mth.lerp(ration, v0, vMiddle);
        float lowV = Mth.lerp(ration, v1, vMiddle);

        for (int i = 0; i < 2; ++i) {
            poseStack.pushPose();
            poseStack.translate((-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees((i * 2 - 1) * 10));
            Matrix4f matrix4f = poseStack.last().pose();
            BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferBuilder.addVertex(matrix4f, -0.5F, -0.5F, -0.5F).setUv(lowU, lowV).setColor(1.0F, 1.0F, 1.0F, 0.9F);
            bufferBuilder.addVertex(matrix4f, 0.5F, -0.5F, -0.5F).setUv(highU, lowV).setColor(1.0F, 1.0F, 1.0F, 0.9F);
            bufferBuilder.addVertex(matrix4f, 0.5F, 0.5F, -0.5F).setUv(highU, highV).setColor(1.0F, 1.0F, 1.0F, 0.9F);
            bufferBuilder.addVertex(matrix4f, -0.5F, 0.5F, -0.5F).setUv(lowU, highV).setColor(1.0F, 1.0F, 1.0F, 0.9F);
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
            poseStack.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }
}

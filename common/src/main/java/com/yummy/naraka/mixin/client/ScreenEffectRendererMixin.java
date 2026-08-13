package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.yummy.naraka.client.renderer.PurifiedSoulFlameRenderMaterial;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
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

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
    @Inject(method = "renderScreenEffect", at = @At("RETURN"))
    private static void renderPurifiedSoulFIre(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        if (minecraft.player != null && !minecraft.player.isSpectator() && minecraft.options.getCameraType().isFirstPerson()
                && EntityDataHelper.getRawEntityData(minecraft.player, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.getConcreteValue()) > 0) {
            naraka$renderPurifiedSoulFire(poseStack);
        }
    }

    @Unique
    private static void naraka$renderPurifiedSoulFire(PoseStack poseStack) {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
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
        float ratio = textureAtlasSprite.uvShrinkRatio();
        float topU = Mth.lerp(ratio, u0, uMiddle);
        float bottomU = Mth.lerp(ratio, u1, uMiddle);
        float topV = Mth.lerp(ratio, v0, vMiddle);
        float bottomV = Mth.lerp(ratio, v1, vMiddle);

        for (int i = 0; i < 2; i++) {
            poseStack.pushPose();
            poseStack.translate((float) (-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) (i * 2 - 1) * 10.0F));
            Matrix4f matrix4f = poseStack.last().pose();
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            bufferBuilder.vertex(matrix4f, -0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(bottomU, bottomV).endVertex();
            bufferBuilder.vertex(matrix4f, 0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(topU, bottomV).endVertex();
            bufferBuilder.vertex(matrix4f, 0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(topU, topV).endVertex();
            bufferBuilder.vertex(matrix4f, -0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(bottomU, topV).endVertex();
            BufferUploader.drawWithShader(bufferBuilder.end());
            poseStack.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }
}

package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.entity.state.StardustRenderState;
import com.yummy.naraka.world.entity.Stardust;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class StardustRenderer extends EntityRenderer<Stardust, StardustRenderState> {
    public StardustRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public StardustRenderState createRenderState() {
        return new StardustRenderState();
    }

    @Override
    public void render(StardustRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            float yHeadRot = Mth.rotLerp(renderState.ageInTicks, player.yHeadRotO, player.yHeadRot);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yHeadRot));
        }
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(NarakaTextures.STARDUST));
        PoseStack.Pose pose = poseStack.last();
        vertexConsumer.addVertex(pose, -1, 1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(0xffffff)
                .setUv(0, 1)
                .setNormal(pose, 0, 0, 1);
        vertexConsumer.addVertex(pose, -1, 0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(0xffffff)
                .setUv(0, 0)
                .setNormal(pose, 0, 0, 1);
        vertexConsumer.addVertex(pose, 0, 0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(0xffffff)
                .setUv(1, 0)
                .setNormal(pose, 0, 0, 1);
        vertexConsumer.addVertex(pose, 0, 1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(0xffffff)
                .setUv(1, 1)
                .setNormal(pose, 0, 0, 1);

        poseStack.popPose();
        super.render(renderState, poseStack, buffer, packedLight);
    }
}

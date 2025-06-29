package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.*;
import net.minecraft.util.ARGB;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class HerobrineSkyRenderHelper {
    private static final float TIME_OF_DAY = 0;
    private static final Color SKY_COLOR = Color.of(1, 0.04f, 0.04f, 0.04f);

    public static void renderHerobrineSky(SkyRenderer skyRenderer, RenderBuffers renderBuffers, FogParameters fog) {
        RenderSystem.setShaderFog(fog);
        PoseStack poseStack = new PoseStack();
        float r = SKY_COLOR.red01();
        float g = SKY_COLOR.green01();
        float b = SKY_COLOR.blue01();
        skyRenderer.renderSkyDisc(r, g, b);
        skyRenderer.renderDarkDisc();
        MultiBufferSource.BufferSource bufferSource = renderBuffers.bufferSource();
        renderEclipse(poseStack, bufferSource);

        bufferSource.endBatch();
    }

    public static void renderEclipse(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(TIME_OF_DAY * 360.0F));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.celestial(NarakaTextures.ECLIPSE));
        int white = ARGB.white(1);
        Matrix4f matrix4f = poseStack.last().pose();
        vertexConsumer.addVertex(matrix4f, -30.0F, 100.0F, -30.0F).setUv(0.0F, 0.0F).setColor(white);
        vertexConsumer.addVertex(matrix4f, 30.0F, 100.0F, -30.0F).setUv(1.0F, 0.0F).setColor(white);
        vertexConsumer.addVertex(matrix4f, 30.0F, 100.0F, 30.0F).setUv(1.0F, 1.0F).setColor(white);
        vertexConsumer.addVertex(matrix4f, -30.0F, 100.0F, 30.0F).setUv(0.0F, 1.0F).setColor(white);
        bufferSource.endBatch();

        poseStack.popPose();
    }
}

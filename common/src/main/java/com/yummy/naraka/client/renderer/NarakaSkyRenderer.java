package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class NarakaSkyRenderer implements DimensionSkyRenderer {
    private final VertexBuffer starBuffer = DimensionSkyRenderer.createBuffer(drawStars(Tesselator.getInstance()));

    private MeshData drawStars(Tesselator tesselator) {
        RandomSource randomSource = RandomSource.create(10842L);
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for (int count = 0; count < 1500; count++) {
            float x = randomSource.nextFloat() * 2.0F - 1.0F;
            float y = randomSource.nextFloat() * 2.0F - 1.0F;
            float z = randomSource.nextFloat() * 2.0F - 1.0F;
            float l = 0.15F + randomSource.nextFloat() * 0.1F;
            float distanceSquare = Mth.lengthSquared(x, y, z);
            if (!(distanceSquare <= 0.010000001F) && !(distanceSquare >= 1.0F)) {
                Vector3f vector3f = new Vector3f(x, y, z).normalize(100.0F);
                float zRot = (float) (randomSource.nextDouble() * (float) Math.PI * 2.0);
                Quaternionf quaternionf = new Quaternionf().rotateTo(new Vector3f(0.0F, 0.0F, -1.0F), vector3f).rotateZ(zRot);
                bufferBuilder.addVertex(vector3f.add(new Vector3f(l, -l, 0.0F).rotate(quaternionf)));
                bufferBuilder.addVertex(vector3f.add(new Vector3f(l, l, 0.0F).rotate(quaternionf)));
                bufferBuilder.addVertex(vector3f.add(new Vector3f(-l, l, 0.0F).rotate(quaternionf)));
                bufferBuilder.addVertex(vector3f.add(new Vector3f(-l, -l, 0.0F).rotate(quaternionf)));
            }
        }

        return bufferBuilder.buildOrThrow();
    }

    @Override
    public void renderSky(ClientLevel level, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup) {
        ShaderInstance positionShader = GameRenderer.getPositionShader();
        if (positionShader != null) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();
            poseStack.mulPose(frustumMatrix);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));

            RenderSystem.setShaderColor(0, 0, 0, 1);
            renderStars(poseStack, projectionMatrix, positionShader);
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
            );
            RenderSystem.setShaderColor(1, 1, 1, 1);
            renderEclipse(poseStack, Tesselator.getInstance(), NarakaTextures.ECLIPSE);
            RenderSystem.defaultBlendFunc();
            renderEclipse(poseStack, Tesselator.getInstance(), NarakaTextures.INVERTED_ECLIPSE);

            poseStack.popPose();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(true);
        }
    }

    private void renderStars(PoseStack poseStack, Matrix4f projectionMatrix, ShaderInstance positionShader) {
        FogRenderer.setupNoFog();
        this.starBuffer.bind();
        this.starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, positionShader);
        VertexBuffer.unbind();
    }

    public static void renderEclipse(PoseStack poseStack, Tesselator tesselator, ResourceLocation texture) {
        PoseStack.Pose pose = poseStack.last();
        RenderSystem.setShaderTexture(0, texture);
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(pose, -30, -95, 30).setUv(1, 1);
        bufferBuilder.addVertex(pose, 30, -95, 30).setUv(0, 1);
        bufferBuilder.addVertex(pose, 30, -95, -30).setUv(0, 0);
        bufferBuilder.addVertex(pose, -30, -95, -30).setUv(1, 0);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }
}

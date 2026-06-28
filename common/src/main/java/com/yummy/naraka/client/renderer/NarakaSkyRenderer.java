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
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;

public class NarakaSkyRenderer implements DimensionSkyRenderer {
    private final VertexBuffer starBuffer = DimensionSkyRenderer.createBuffer(drawStars());

    private BufferBuilder.RenderedBuffer drawStars() {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();

        RandomSource randomSource = RandomSource.create(10842L);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for (int i = 0; i < 1500; i++) {
            double d = randomSource.nextFloat() * 2.0F - 1.0F;
            double e = randomSource.nextFloat() * 2.0F - 1.0F;
            double f = randomSource.nextFloat() * 2.0F - 1.0F;
            double g = 0.15F + randomSource.nextFloat() * 0.1F;
            double h = d * d + e * e + f * f;
            if (h < 1.0 && h > 0.01) {
                h = 1.0 / Math.sqrt(h);
                d *= h;
                e *= h;
                f *= h;
                double j = d * 100.0;
                double k = e * 100.0;
                double l = f * 100.0;
                double m = Math.atan2(d, f);
                double n = Math.sin(m);
                double o = Math.cos(m);
                double p = Math.atan2(Math.sqrt(d * d + f * f), e);
                double q = Math.sin(p);
                double r = Math.cos(p);
                double s = randomSource.nextDouble() * Math.PI * 2.0;
                double t = Math.sin(s);
                double u = Math.cos(s);

                for (int v = 0; v < 4; v++) {
                    double w = 0.0;
                    double x = ((v & 2) - 1) * g;
                    double y = ((v + 1 & 2) - 1) * g;
                    double z = 0.0;
                    double aa = x * u - y * t;
                    double ab = y * u + x * t;
                    double ad = aa * q + 0.0 * r;
                    double ae = 0.0 * q - aa * r;
                    double af = ae * n - ab * o;
                    double ah = ab * n + ae * o;
                    builder.vertex(j + af, k + ad, l + ah).endVertex();
                }
            }
        }

        return builder.end();
    }

    @Override
    public void renderSky(ClientLevel level, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup) {
        ShaderInstance positionShader = GameRenderer.getPositionShader();
        if (positionShader != null) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));

            RenderSystem.setShaderColor(1, 1, 1, 1);
            renderStars(poseStack, projectionMatrix, positionShader);
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
            );
            RenderSystem.setShaderColor(1, 1, 1, 1);
            renderEclipse(poseStack, Tesselator.getInstance(), NarakaTextures.ECLIPSE);

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
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(pose.pose(), -30, -95, 30).uv(1, 1);
        bufferBuilder.vertex(pose.pose(), 30, -95, 30).uv(0, 1);
        bufferBuilder.vertex(pose.pose(), 30, -95, -30).uv(0, 0);
        bufferBuilder.vertex(pose.pose(), -30, -95, -30).uv(1, 0);
        BufferUploader.drawWithShader(bufferBuilder.end());
    }
}

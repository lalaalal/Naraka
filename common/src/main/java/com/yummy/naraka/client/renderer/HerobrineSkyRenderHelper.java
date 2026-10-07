package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.renderer.SkyRenderer;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Optional;
import java.util.OptionalDouble;

public class HerobrineSkyRenderHelper {
    private static final Vector3fc SKY_COLOR = new Vector3f(0.04f, 0.04f, 0.04f);

    public static void renderHerobrineSky(RenderTarget renderTarget, SkyRenderer skyRenderer, GpuBufferSlice gpuBufferSlice) {
        GpuTextureView colorTextureView = renderTarget.getColorTextureView();
        GpuTextureView depthTextureView = renderTarget.getDepthTextureView();
        if (colorTextureView == null || depthTextureView == null)
            return;
        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Sky eclipse", colorTextureView, Optional.empty(), depthTextureView, OptionalDouble.empty())) {
            RenderSystem.bindDefaultUniforms(renderPass);
            PoseStack poseStack = new PoseStack();
            skyRenderer.renderSkyDisc(renderPass, SKY_COLOR);
            poseStack.pushPose();
            poseStack.rotate(Axis.YP.rotationDegrees(-90.0F));
            NarakaSkyRenderer.getInstance()
                    .renderEclipse(renderPass, poseStack);
            poseStack.popPose();
        }
    }
}

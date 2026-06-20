package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.util.Color;
import net.minecraft.client.renderer.SkyRenderer;

public class HerobrineSkyRenderHelper {
    private static final Color SKY_COLOR = Color.of(1, 0.04f, 0.04f, 0.04f);

    public static void renderHerobrineSky(SkyRenderer skyRenderer, GpuBufferSlice gpuBufferSlice) {
        RenderSystem.setShaderFog(gpuBufferSlice);
        PoseStack poseStack = new PoseStack();
        skyRenderer.renderSkyDisc(SKY_COLOR.pack());
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        NarakaSkyRenderer.getInstance()
                .renderEclipse(poseStack);
        poseStack.popPose();
    }
}

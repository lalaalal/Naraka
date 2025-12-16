package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.SkyRenderState;

@Environment(EnvType.CLIENT)
public class HerobrineSkyRenderHelper {
    private static final Color SKY_COLOR = Color.of(1, 0.04f, 0.04f, 0.04f);

    public static void renderHerobrineSky(SkyRenderer skyRenderer, SkyRenderState skyRenderState, GpuBufferSlice gpuBufferSlice) {
        RenderSystem.setShaderFog(gpuBufferSlice);
        PoseStack poseStack = new PoseStack();
        float r = SKY_COLOR.red01();
        float g = SKY_COLOR.green01();
        float b = SKY_COLOR.blue01();
        skyRenderer.renderSkyDisc(Color.of(1, r, g, b).pack());
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        NarakaSkyRenderer.getInstance()
                .renderEclipse(poseStack, NarakaTextures.ECLIPSE, RenderPipelines.CELESTIAL);
        poseStack.popPose();
    }
}

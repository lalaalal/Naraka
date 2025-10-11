package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.SkyRenderState;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public class HerobrineSkyRenderHelper {
    private static final Color SKY_COLOR = Color.of(1, 0.04f, 0.04f, 0.04f);
    private static BiConsumer<Float, PoseStack> eclipseRenderer = (rainBrightness, poseStack) -> {
    };

    public static void setEclipseRenderer(BiConsumer<Float, PoseStack> eclipseRenderer) {
        HerobrineSkyRenderHelper.eclipseRenderer = eclipseRenderer;
    }

    public static void renderHerobrineSky(SkyRenderer skyRenderer, SkyRenderState skyRenderState, GpuBufferSlice gpuBufferSlice) {
        RenderSystem.setShaderFog(gpuBufferSlice);
        PoseStack poseStack = new PoseStack();
        float r = SKY_COLOR.red01();
        float g = SKY_COLOR.green01();
        float b = SKY_COLOR.blue01();
        skyRenderer.renderSkyDisc(r, g, b);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        eclipseRenderer.accept(skyRenderState.rainBrightness, poseStack);
        poseStack.popPose();
    }
}

package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.StardustModel;
import com.yummy.naraka.client.renderer.entity.state.LightTailEntityRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.Stardust;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class StardustRenderer extends LightTailEntityRenderer<Stardust, LightTailEntityRenderState> {
    private final StardustModel model;

    public StardustRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new StardustModel(context.bakeLayer(NarakaModelLayers.STARDUST));
    }

    @Override
    public LightTailEntityRenderState createRenderState() {
        return new LightTailEntityRenderState();
    }

    @Override
    public void submit(LightTailEntityRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        float rotation = renderState.ageInTicks * renderState.ageInTicks * 0.1f;
        poseStack.translate(0, 0.25, 0);
        poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), NarakaRenderUtils.SIN_45, 0.0F, NarakaRenderUtils.SIN_45));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation * 2));
        submitNodeCollector.submitModel(
                model,
                renderState,
                poseStack,
                RenderType.entityCutout(NarakaTextures.STARDUST),
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1,
                null,
                renderState.outlineColor,
                null);
        poseStack.popPose();
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
    }
}

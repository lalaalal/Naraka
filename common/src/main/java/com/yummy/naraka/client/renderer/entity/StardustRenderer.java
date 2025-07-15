package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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
    public void render(LightTailEntityRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float rotation = renderState.ageInTicks * renderState.ageInTicks * 0.1f;
        poseStack.translate(0, 0.25, 0);
        poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), NarakaRenderUtils.SIN_45, 0.0F, NarakaRenderUtils.SIN_45));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation * 2));

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(NarakaTextures.STARDUST));
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(renderState, poseStack, buffer, packedLight);
    }
}

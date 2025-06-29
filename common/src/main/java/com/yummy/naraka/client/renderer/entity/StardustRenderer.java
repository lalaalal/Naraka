package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.StardustModel;
import com.yummy.naraka.client.renderer.entity.state.FlatImageRenderState;
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
public class StardustRenderer extends EntityRenderer<Stardust, FlatImageRenderState> {
    private final StardustModel model;

    public StardustRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new StardustModel(context.bakeLayer(NarakaModelLayers.STARDUST));
    }

    @Override
    public FlatImageRenderState createRenderState() {
        return new FlatImageRenderState();
    }

    @Override
    public void extractRenderState(Stardust entity, FlatImageRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            reusedState.yRot = Mth.rotLerp(partialTick, player.yHeadRotO, player.yHeadRot);
        }
    }

    @Override
    public void render(FlatImageRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(-0.75, -1.625, -1.25);
        poseStack.mulPose(Axis.YN.rotationDegrees(45));
        poseStack.mulPose(Axis.ZN.rotationDegrees(45));

        poseStack.scale(2, 2, 2);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(NarakaTextures.STARDUST));
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(renderState, poseStack, buffer, packedLight);
    }
}

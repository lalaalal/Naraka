package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.entity.NarakaEntities;
import com.yummy.naraka.entity.Spear;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;

public class SpearRenderer extends EntityRenderer<Spear> {
    private final SpearModel model;

    public SpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new SpearModel(context.bakeLayer(NarakaModelLayers.SPEAR));
    }

    @Override
    public ResourceLocation getTextureLocation(Spear spear) {
        HolderSet<EntityType<?>> mightyHolySpearType = HolderSet.direct(NarakaEntities.THROWN_MIGHTY_HOLY_SPEAR);
        if (spear.getType().is(mightyHolySpearType))
            return NarakaTextures.MIGHTY_HOLY_SPEAR;
        return NarakaTextures.SPEAR;
    }

    @Override
    public void render(Spear spear, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, spear.yRotO, spear.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, spear.xRotO, spear.getXRot()) + 90.0F));
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(getTextureLocation(spear)));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
    }
}

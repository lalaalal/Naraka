package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.Platform;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class HerobrineEyeLayer<T extends AbstractHerobrine, M extends AbstractHerobrineModel<T>> extends RenderLayer<T, M> {
    public HerobrineEyeLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    private RenderType getRenderType(T entity) {
        if (Platform.getInstance().getModLoader() == Platform.ModLoader.NEO_FORGE)
            return RenderType.entityTranslucent(getEyeTexture(entity));
        return RenderType.entityTranslucentEmissive(getEyeTexture(entity));
    }

    private ResourceLocation getEyeTexture(T entity) {
        if (entity.isFinalModel())
            return NarakaTextures.FINAL_HEROBRINE_EYE;
        return NarakaTextures.HEROBRINE_EYE;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(getRenderType(livingEntity));
        getParentModel().renderForEye(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, livingEntity.getEyeAlpha());
        poseStack.popPose();
    }
}

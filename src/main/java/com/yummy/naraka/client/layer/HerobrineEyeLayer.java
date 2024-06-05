package com.yummy.naraka.client.layer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.entity.Herobrine;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HerobrineEyeLayer<T extends Herobrine> extends RenderLayer<T, HerobrineModel<T>> {
    public HerobrineEyeLayer(RenderLayerParent<T, HerobrineModel<T>> renderer) {
        super(renderer);
    }

    private RenderType getRenderType() {
        return RenderType.entityCutout(NarakaTextures.HEROBRINE_EYE);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T herobrine, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer vertexConsumer = buffer.getBuffer(getRenderType());
        HerobrineModel<T> model = getParentModel();
        model.renderToBuffer(poseStack, vertexConsumer, 240, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    @Override
    protected ResourceLocation getTextureLocation(T herobrine) {
        return NarakaTextures.HEROBRINE_EYE;
    }
}

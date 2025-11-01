package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineCrackLayer extends RenderLayer<ShadowHerobrine, AbstractHerobrineModel<ShadowHerobrine>> {
    private static final ResourceLocation[] CRACK_TEXTURES = {
            NarakaTextures.SHADOW_HEROBRINE_25,
            NarakaTextures.SHADOW_HEROBRINE_50,
            NarakaTextures.SHADOW_HEROBRINE_75,
    };

    public ShadowHerobrineCrackLayer(RenderLayerParent<ShadowHerobrine, AbstractHerobrineModel<ShadowHerobrine>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ShadowHerobrine livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        float healthRatio = livingEntity.getHealth() / livingEntity.getMaxHealth();
        int crack = Mth.floor(healthRatio / 0.25f);

        if (crack >= CRACK_TEXTURES.length)
            return;

        poseStack.pushPose();
        RenderType renderType = RenderType.entityTranslucent(CRACK_TEXTURES[crack]);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        getParentModel().root().render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
    }
}

package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.Herobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer extends LivingEntityRenderer<Herobrine, HerobrineModel<Herobrine>> {
    public HerobrineRenderer(EntityRendererProvider.Context context) {
        this(context, NarakaModelLayers.HEROBRINE);
    }

    public HerobrineRenderer(EntityRendererProvider.Context context, ModelLayerLocation layerLocation) {
        super(context, new HerobrineModel<>(context.bakeLayer(layerLocation)), 0.5f);
        addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    protected boolean shouldShowName(Herobrine herobrine) {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(Herobrine herobrine) {
        return NarakaTextures.HEROBRINE;
    }

    @Override
    public void render(Herobrine herobrine, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        for (Afterimage afterimage : herobrine.getAfterimages())
            this.renderAfterimage(herobrine, afterimage, partialTicks, poseStack, buffer, packedLight);
        super.render(herobrine, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    protected void renderAfterimage(Herobrine herobrine, Afterimage afterimage, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        Vec3 translation = afterimage.translation(herobrine);
        poseStack.translate(translation.x, translation.y + 1.5, translation.z);
        poseStack.scale(1, -1, 1);

        float yBodyRot = Mth.rotLerp(partialTicks, herobrine.yBodyRotO, herobrine.yBodyRot);
        this.setupRotations(herobrine, poseStack, getBob(herobrine, partialTicks), yBodyRot, partialTicks, herobrine.getScale());

        RenderType renderType = RenderType.entityTranslucent(getTextureLocation(herobrine));
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        int color = Color.of(0x0000aa).withAlpha(afterimage.getAlpha());
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
    }
}

package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public abstract class AfterimageEntityRenderer<T extends LivingEntity & AfterimageEntity, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {
    public AfterimageEntityRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        for (Afterimage afterimage : entity.getAfterimages())
            this.renderAfterimage(entity, afterimage, partialTicks, poseStack, buffer, packedLight);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    protected void renderAfterimage(T entity, Afterimage afterimage, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        Vec3 translation = afterimage.translation(entity);
        poseStack.translate(translation.x, translation.y + 1.5, translation.z);
        poseStack.scale(1, -1, 1);

        float yBodyRot = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        this.setupRotations(entity, poseStack, getBob(entity, partialTicks), yBodyRot, partialTicks, entity.getScale());

        RenderType renderType = RenderType.entityTranslucent(getTextureLocation(entity));
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        int color = Color.of(0xddddff).withAlpha(afterimage.getAlpha()).pack();
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
    }
}

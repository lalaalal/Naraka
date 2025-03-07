package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class AfterimageEntityRenderer<T extends LivingEntity & AfterimageEntity, M extends HierarchicalModel<T>> extends LivingEntityRenderer<T, M> {
    private final M afterimageModel;

    public AfterimageEntityRenderer(EntityRendererProvider.Context context, Supplier<M> model, float shadowRadius) {
        super(context, model.get(), shadowRadius);
        this.afterimageModel = model.get();
    }

    @Override
    public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
        return !entity.getAfterimages().isEmpty() || super.shouldRender(entity, camera, camX, camY, camZ);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        for (Afterimage afterimage : entity.getAfterimages())
            this.renderAfterimage(entity, afterimage, partialTicks, poseStack, buffer, LightTexture.FULL_BRIGHT);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    protected void renderAfterimage(T entity, Afterimage afterimage, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        Vec3 translation = afterimage.translation(entity, partialTicks);
        poseStack.translate(translation.x, translation.y + 1.5, translation.z);
        poseStack.scale(1, -1, 1);

        this.setupRotations(entity, poseStack, getBob(entity, partialTicks), afterimage.getYRot(), partialTicks, entity.getScale());

        RenderType renderType = RenderType.entityTranslucent(getAfterimageTexture(entity));
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        int color = Color.of(0xffffff).withAlpha(afterimage.getAlpha()).pack();
        this.afterimageModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
    }

    protected abstract ResourceLocation getAfterimageTexture(T entity);
}

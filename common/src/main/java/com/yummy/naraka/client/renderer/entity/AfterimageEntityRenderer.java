package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

@Environment(EnvType.CLIENT)
public abstract class AfterimageEntityRenderer<T extends LivingEntity & AfterimageEntity, M extends EntityModel<T>>
        extends LivingEntityRenderer<T, M> {
    protected AfterimageEntityRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
        return !entity.getAfterimages().isEmpty() || super.shouldRender(entity, camera, camX, camY, camZ);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Collection<Afterimage> afterimages = entity.getAfterimages();
        if (!afterimages.isEmpty()) {
            int blockLight = Mth.clamp(afterimages.size(), LightTexture.block(packedLight), 15);
            int skyLight = Mth.clamp(afterimages.size(), LightTexture.sky(packedLight), 15);
            packedLight = Math.max(LightTexture.pack(blockLight, skyLight), packedLight);
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        renderAfterimages(entity, partialTicks, poseStack, buffer);
    }

    protected void renderAfterimages(T entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        for (Afterimage afterimage : entity.getAfterimages())
            this.renderAfterimage(entity, afterimage, partialTick, poseStack, bufferSource);
    }

    protected void renderAfterimage(T entity, Afterimage afterimage, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        int alpha = afterimage.getAlpha(partialTick);
        if (alpha == 0 && afterimage.getPartialTicks() < partialTick)
            return;

        Vec3 translation = afterimage.translation(entity, partialTick);

        poseStack.pushPose();
        poseStack.translate(translation.x, translation.y + 1.5, translation.z);
        poseStack.scale(1, -1, 1);

        this.setupRotations(entity, poseStack, getBob(entity, partialTick), afterimage.getYRot(), partialTick, entity.getScale());

        Color color = getAfterimageColor(afterimage, partialTick);
        int light = (int) (color.alpha01() * 5);
        int packedLight = LightTexture.pack(light, light);
        RenderType renderType = RenderType.entityTranslucent(getAfterimageTexture(entity));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

        getAfterimageModel(entity).renderToBuffer(
                poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color.pack()
        );
        renderAfterimageLayer(entity, afterimage, poseStack, bufferSource, packedLight, color.alpha());

        poseStack.popPose();
    }

    protected Color getAfterimageColor(Afterimage afterimage, float partialTicks) {
        int alpha = afterimage.getAlpha(partialTicks);
        return NarakaConfig.CLIENT.afterimageColor.getValue().withAlpha(alpha);
    }

    protected void renderAfterimageLayer(T entity, Afterimage afterimage, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int alpha) {

    }

    protected abstract M getAfterimageModel(T entity);

    protected abstract ResourceLocation getAfterimageTexture(T entity);
}

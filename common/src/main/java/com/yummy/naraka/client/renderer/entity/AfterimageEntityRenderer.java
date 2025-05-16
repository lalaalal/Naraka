package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.renderer.entity.state.AfterimageRenderState;
import com.yummy.naraka.util.Color;
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
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class AfterimageEntityRenderer<T extends LivingEntity & AfterimageEntity, S extends LivingEntityRenderState & AfterimageRenderState.Provider, M extends EntityModel<S>>
        extends LivingEntityRenderer<T, S, M> {
    protected final M afterimageModel;

    public AfterimageEntityRenderer(EntityRendererProvider.Context context, Supplier<M> model, float shadowRadius) {
        super(context, model.get(), shadowRadius);
        this.afterimageModel = model.get();
    }

    @Override
    public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
        return !entity.getAfterimages().isEmpty() || super.shouldRender(entity, camera, camX, camY, camZ);
    }

    @Override
    public void render(S renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Collection<AfterimageRenderState> afterimages = renderState.afterimages();
        if (!afterimages.isEmpty()) {
            int blockLight = Mth.clamp(afterimages.size(), LightTexture.block(packedLight), 15);
            int skyLight = Mth.clamp(afterimages.size(), LightTexture.sky(packedLight), 15);

            packedLight = Math.max(LightTexture.pack(blockLight, skyLight), packedLight);
        }
        renderAfterimages(renderState, poseStack, buffer);
        super.render(renderState, poseStack, buffer, packedLight);
    }

    protected void renderAfterimages(S renderState, PoseStack poseStack, MultiBufferSource buffer) {
        for (AfterimageRenderState afterimage : renderState.afterimages())
            this.renderAfterimage(renderState, afterimage, poseStack, buffer);
    }

    protected void renderAfterimage(S renderState, AfterimageRenderState afterimageRenderState, PoseStack poseStack, MultiBufferSource buffer) {
        if (!afterimageRenderState.canRender)
            return;

        Vec3 translation = afterimageRenderState.translation;

        poseStack.pushPose();
        poseStack.translate(translation.x, translation.y + 1.5, translation.z);
        poseStack.scale(1, -1, 1);

        this.setupRotations(renderState, poseStack, afterimageRenderState.bodyRot, renderState.scale);

        RenderType renderType = RenderType.entityTranslucentEmissive(getAfterimageTexture(renderState), true);
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        Color color = afterimageRenderState.color;
        int light = (int) (color.alpha01() * 5);
        int packedLight = LightTexture.pack(light, light);

        this.afterimageModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color.pack());
        renderAfterimageLayer(renderState, afterimageRenderState, poseStack, buffer, packedLight, color.alpha());

        poseStack.popPose();
    }

    protected void renderAfterimageLayer(S renderState, AfterimageRenderState afterimage, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int alpha) {

    }

    protected abstract ResourceLocation getAfterimageTexture(S afterimage);
}

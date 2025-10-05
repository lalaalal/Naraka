package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.entity.state.AfterimageRenderState;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.AfterimageEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

@Environment(EnvType.CLIENT)
public abstract class AfterimageEntityRenderer<T extends LivingEntity & AfterimageEntity, S extends LivingEntityRenderState & AfterimageRenderState.Provider, M extends EntityModel<S>>
        extends LivingEntityRenderer<T, S, M> {
    protected AfterimageEntityRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
        return !entity.getAfterimages().isEmpty() || super.shouldRender(entity, camera, camX, camY, camZ);
    }

    @Override
    public void submit(S renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        Collection<AfterimageRenderState> afterimages = renderState.afterimages();
        int packedLight = renderState.lightCoords;
        if (!afterimages.isEmpty()) {
            int blockLight = Mth.clamp(afterimages.size(), LightTexture.block(packedLight), 15);
            int skyLight = Mth.clamp(afterimages.size(), LightTexture.sky(packedLight), 15);
            renderState.lightCoords = Math.max(LightTexture.pack(blockLight, skyLight), packedLight);
        }
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
        submitAfterimages(renderState, poseStack, submitNodeCollector);
    }

    protected void submitAfterimages(S renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        for (AfterimageRenderState afterimage : renderState.afterimages())
            this.submitAfterimage(renderState, afterimage, poseStack, submitNodeCollector);
    }

    protected void submitAfterimage(S renderState, AfterimageRenderState afterimageRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        if (!afterimageRenderState.canRender)
            return;

        Vec3 translation = afterimageRenderState.translation;

        poseStack.pushPose();
        poseStack.translate(translation.x, translation.y + 1.5, translation.z);
        poseStack.scale(1, -1, 1);

        this.setupRotations(renderState, poseStack, afterimageRenderState.bodyRot, renderState.scale);

        RenderType renderType = RenderType.entityTranslucent(getAfterimageTexture(renderState), true);
        Color color = afterimageRenderState.color;
        int light = (int) (color.alpha01() * 5);
        int packedLight = LightTexture.pack(light, light);

        submitNodeCollector.submitModel(
                getAfterimageModel(renderState),
                renderState, poseStack, renderType,
                packedLight, OverlayTexture.NO_OVERLAY, color.pack(),
                null,
                afterimageRenderState.outlineColor,
                null
        );
        submitAfterimageLayer(renderState, afterimageRenderState, poseStack, submitNodeCollector, packedLight, color.alpha());

        poseStack.popPose();
    }

    protected void submitAfterimageLayer(S renderState, AfterimageRenderState afterimage, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int alpha) {

    }

    protected abstract M getAfterimageModel(S renderState);

    protected abstract ResourceLocation getAfterimageTexture(S renderState);
}

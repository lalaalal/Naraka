package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.PickaxeSlash;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public class PickaxeSlashRenderer extends LightTailEntityRenderer<PickaxeSlash> {
    public PickaxeSlashRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(PickaxeSlash entity) {
        return NarakaTextures.PICKAXE_SLASH;
    }

    @Override
    public void render(PickaxeSlash entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        int color = entity.getColor(partialTick);
        float yRot = entity.getViewYRot(partialTick);
        float zRot = entity.getZRot();

        poseStack.pushPose();
        poseStack.scale(6, 6, 6);
        poseStack.translate(0, -0.25, 0);
        poseStack.mulPose(Axis.YN.rotationDegrees(yRot));
        poseStack.rotateAround(Axis.ZN.rotationDegrees(zRot), 0, 0.5f, 0);
        poseStack.translate(0, 0, -0.25);
        RenderType renderType = RenderType.entityTranslucent(getTextureLocation(entity));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        NarakaRenderUtils.renderFlatImage(poseStack.last(), vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color, Direction.Axis.X);
        poseStack.popPose();
    }

    @Override
    protected void renderTail(PickaxeSlash entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, Vec3 translation) {
        Vector3f vector3f = new Vector3f(0, 0, 1)
                .rotate(Axis.ZN.rotationDegrees(entity.getZRot()))
                .rotate(Axis.YN.rotationDegrees(entity.getViewYRot(partialTick)));
        super.renderTail(entity, partialTick, poseStack, bufferSource, new Vec3(vector3f).add(translation).add(0, 1.2, 0));
    }

    @Override
    protected void renderTailPart(PickaxeSlash entity, float partialTick, PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector3f from, Vector3f to, float index, float size, int color) {
        float yRot = entity.getViewYRot(partialTick);
        float zRot = entity.getZRot();
        NarakaRenderUtils.renderFlatImage(pose, vertexConsumer,
                NarakaRenderUtils.createVertices(from, to, 1.5f, modifier(yRot, zRot)), index, index, size, size,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color
        );
    }

    protected BiFunction<Vector3f, Float, Vector3f> modifier(float yRot, float zRot) {
        return (vector, interval) -> {
            Vector3f result = new Vector3f(0, interval, 0)
                    .rotate(Axis.ZN.rotationDegrees(zRot))
                    .rotate(Axis.YN.rotationDegrees(yRot));
            return result.add(vector);
        };
    }
}

package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.HerobrineScarfModel;
import com.yummy.naraka.client.renderer.entity.state.HerobrineRenderState;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfRenderState;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfTexture;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.ScarfWavingData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class HerobrineScarfLayer extends RenderLayer<HerobrineRenderState, AbstractHerobrineModel<HerobrineRenderState>> {
    private final HerobrineScarfModel scarfModel;

    public HerobrineScarfLayer(RenderLayerParent<HerobrineRenderState, AbstractHerobrineModel<HerobrineRenderState>> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.scarfModel = new HerobrineScarfModel(context.bakeLayer(NarakaModelLayers.HEROBRINE_SCARF));
    }

    private static void applyTranslateAndRotate(PoseStack poseStack, AbstractHerobrineModel<HerobrineRenderState> herobrineModel) {
        herobrineModel.main().translateAndRotate(poseStack);
        herobrineModel.upperBody().translateAndRotate(poseStack);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, HerobrineRenderState renderState, float yRot, float xRot) {
        if (!renderState.renderScarf)
            return;
        poseStack.pushPose();
        if (renderState.phase < 3) {
            RenderType renderType = RenderType.entitySmoothCutout(NarakaTextures.HEROBRINE_SCARF);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            applyTranslateAndRotate(poseStack, getParentModel());

            scarfModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }

        RenderType waveRenderType = RenderType.entityCutout(renderState.scarfRenderState.textureInfo.texture());
        VertexConsumer vertexConsumer = bufferSource.getBuffer(waveRenderType);
        float scale = renderState.scarfRenderState.scale;
        Vec3 translation = renderState.scarfRenderState.translation;
        poseStack.scale(-scale, scale, scale);
        poseStack.translate(translation);

        renderScarf(poseStack, vertexConsumer, packedLight, renderState.scarfRenderState);

        poseStack.popPose();
    }

    public void renderScarf(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, WavingScarfRenderState renderState) {
        WavingScarfTexture textureInfo = renderState.textureInfo;
        ScarfWavingData waveData = renderState.waveData;

        poseStack.pushPose();
        poseStack.rotateAround(Axis.XP.rotationDegrees(renderState.rotationDegree), 0, 0, 0);

        float partialTick = renderState.partialTick;

        int verticalSize = waveData.getVerticalSize();
        int horizontalSize = waveData.getHorizontalSize();

        float partWidth = textureInfo.widthInRatio() / horizontalSize;
        float partHeight = textureInfo.heightInRatio() / verticalSize;

        float u = textureInfo.u();
        float v = textureInfo.v();

        float baseY = waveData.getVerticalPosition(-1, partialTick);
        float shift = 0;
        for (int vertical = 0; vertical < verticalSize; vertical++) {
            poseStack.pushPose();
            float partShift = waveData.getVerticalShift(vertical, partialTick);
            shift += partShift;
            poseStack.translate(shift, -baseY, partHeight * vertical);

            for (int horizontal = 0; horizontal < horizontalSize; horizontal++) {
                poseStack.pushPose();
                poseStack.translate(partWidth * horizontal, 0, 0);

                float topLeft_y = waveData.getVerticalPosition(vertical - 1, partialTick);
                float topRight_y = waveData.getVerticalPosition(vertical - 1, partialTick);
                float bottomLeft_y = waveData.getVerticalPosition(vertical, partialTick);
                float bottomRight_y = waveData.getVerticalPosition(vertical, partialTick);

                List<Vector3f> vertices = List.of(
                        new Vector3f(partShift, bottomLeft_y, partHeight),
                        new Vector3f(0, topLeft_y, 0),
                        new Vector3f(partWidth, topRight_y, 0),
                        new Vector3f(partWidth + partShift, bottomRight_y, partHeight)
                );

                float currentU = u + partWidth * horizontal;
                float currentV = v + partHeight * vertical;

                vertices(vertexConsumer, poseStack.last(), vertices, currentU, currentV, partWidth, partHeight, packedLight, OverlayTexture.NO_OVERLAY, -1, Direction.UP);
                vertices(vertexConsumer, poseStack.last(), vertices, currentU, currentV, partWidth, partHeight, packedLight, OverlayTexture.NO_OVERLAY, -1, Direction.DOWN);
                poseStack.popPose();
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    /**
     * Add 4 vertices in anti-clockwise from left-top based on a positive direction
     *
     * @param positions Positions, size must be 4
     */
    private static void vertices(VertexConsumer vertexConsumer, PoseStack.Pose pose, List<Vector3f> positions, float u, float v, float width, float height, int packedLight, int packedOverlay, int color, Direction direction) {
        Vec3i normal = direction.getUnitVec3i();
        List<Vector2f> uvs = List.of(
                new Vector2f(u, v + height),
                new Vector2f(u, v),
                new Vector2f(u + width, v),
                new Vector2f(u + width, v + height)
        );
        NarakaUtils.iterate(positions, uvs, (position, uv) -> {
            vertexConsumer.addVertex(pose, position)
                    .setColor(color)
                    .setLight(packedLight)
                    .setOverlay(packedOverlay)
                    .setUv(uv.x, uv.y)
                    .setNormal(pose, normal.getX(), normal.getY(), normal.getZ());
        }, normal.getX() < 0 || normal.getY() < 0 || normal.getZ() < 0);
    }
}

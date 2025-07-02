package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.StardustModel;
import com.yummy.naraka.client.renderer.entity.state.StardustRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.Stardust;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class StardustRenderer extends EntityRenderer<Stardust, StardustRenderState> {
    private static final int MAX_TAIL_ALPHA = 0xff;

    private final StardustModel model;

    public StardustRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new StardustModel(context.bakeLayer(NarakaModelLayers.STARDUST));
    }

    @Override
    public StardustRenderState createRenderState() {
        return new StardustRenderState();
    }

    @Override
    public void extractRenderState(Stardust entity, StardustRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.tailPositions = entity.getTailPositions()
                .stream()
                .map(position -> position.subtract(entity.position()))
                .map(NarakaRenderUtils::vector3f)
                .toList();
        reusedState.partialTranslation = entity.position()
                .subtract(entity.getPosition(partialTick));
    }

    @Override
    protected AABB getBoundingBoxForCulling(Stardust stardust) {
        AABB boundingBox = super.getBoundingBoxForCulling(stardust);
        double maxX = boundingBox.maxX, maxY = boundingBox.maxY, maxZ = boundingBox.maxZ;
        double minX = boundingBox.minX, minY = boundingBox.minY, minZ = boundingBox.minZ;
        for (Vec3 position : stardust.getTailPositions()) {
            maxX = Math.max(position.x, maxX);
            maxY = Math.max(position.y, maxY);
            maxZ = Math.max(position.z, maxZ);
            minX = Math.min(position.x, minX);
            minY = Math.min(position.y, minY);
            minZ = Math.min(position.z, minZ);
        }
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void render(StardustRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float rotation = renderState.ageInTicks * renderState.ageInTicks * 0.1f;
        poseStack.translate(0, 0.25, 0);
        poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), NarakaRenderUtils.SIN_45, 0.0F, NarakaRenderUtils.SIN_45));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation * 2));

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(NarakaTextures.STARDUST));
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        super.render(renderState, poseStack, buffer, packedLight);
        poseStack.popPose();
        renderTail(renderState, poseStack, buffer);
    }

    private void renderTail(StardustRenderState renderState, PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);
        poseStack.translate(renderState.partialTranslation);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.beaconBeam(BeaconRenderer.BEAM_LOCATION, true));
        float partSize = 1 / (float) renderState.tailPositions.size();
        for (int index = 0; index < renderState.tailPositions.size() - 1; index++) {
            Vector3f from = renderState.tailPositions.get(index);
            Vector3f to = renderState.tailPositions.get(index + 1);
            float uv = index / (float) renderState.tailPositions.size();
            renderTailPart(from, to, poseStack, vertexConsumer, uv, uv, partSize, partSize, (int) (MAX_TAIL_ALPHA * (1 - uv)));
        }

        poseStack.popPose();
    }

    private void renderTailPart(Vector3f from, Vector3f to, PoseStack poseStack, VertexConsumer vertexConsumer, float u, float v, float width, float height, int alpha) {
        List<Vector3f> vertices = List.of(
                new Vector3f(from.x, from.y + 0.25f, from.z),
                new Vector3f(from.x, from.y - 0.25f, from.z),
                new Vector3f(to.x, to.y - 0.25f, to.z),
                new Vector3f(to.x, to.y + 0.25f, to.z)
        );
        NarakaRenderUtils.renderFlatImage(poseStack, vertexConsumer, vertices, u, v, width, height, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ARGB.color(alpha, 0xED7419));
    }
}

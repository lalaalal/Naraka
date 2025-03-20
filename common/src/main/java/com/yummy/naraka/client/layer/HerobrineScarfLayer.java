package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class HerobrineScarfLayer extends RenderLayer<Herobrine, HerobrineModel<Herobrine>> {
    private static final Vector3f[] FLAT_PLANE_DOWN = new Vector3f[]{
            new Vector3f(-2, 0, 0),
            new Vector3f(-2, 0, -1),
            new Vector3f(2, 0, -1),
            new Vector3f(2, 0, 0)
    };
    private static final Vector3f[] FLAT_PLANE_UP = new Vector3f[]{
            new Vector3f(-2, 0, 0),
            new Vector3f(2, 0, 0),
            new Vector3f(2, 0, -1),
            new Vector3f(-2, 0, -1)
    };

    public HerobrineScarfLayer(RenderLayerParent<Herobrine, HerobrineModel<Herobrine>> renderer, EntityRendererProvider.Context context) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Herobrine herobrine, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        poseStack.translate(0, 0, 1);
        RenderType renderType = RenderType.entityCutout(getTextureLocation(herobrine));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        float offsetY = 0;
        float offsetZ = 0;
        float degree = NarakaMod.config().herobrineScarfWaveMaxAngle.getValue();
        float speed = NarakaMod.config().herobrineScarfWaveSpeed.getValue();
        for (int index = 0; index < 16; index++) {
            poseStack.pushPose();
            float rot = (Mth.cos((ageInTicks - index) * speed) + 1) * (float) Math.toRadians(degree) / 2;
            poseStack.translate(0, offsetY, offsetZ);
            poseStack.rotateAround(Axis.XP.rotation(rot), 0, 0, -1);
            vertices(vertexConsumer, poseStack.last(), FLAT_PLANE_DOWN, 0.0625f, 0.0625f, 0.25f, 0.0625f, packedLight, OverlayTexture.NO_OVERLAY, 0xffffffff, Direction.DOWN);
            vertices(vertexConsumer, poseStack.last(), FLAT_PLANE_UP, 0.0625f, 0.0625f, 0.25f, 0.0625f, packedLight, OverlayTexture.NO_OVERLAY, 0xffffffff, Direction.UP);

            offsetY -= Mth.sin(rot);
            offsetZ += Mth.cos(rot);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private static void vertices(VertexConsumer vertexConsumer, PoseStack.Pose pose, Vector3f[] positions, float u, float v, float width, float height, int packedLight, int packedOverlay, int color, Direction direction) {
        Vec3i normal = direction.getNormal();
        Vector2f[] uvs = new Vector2f[]{
                new Vector2f(u, v),
                new Vector2f(u, v + height),
                new Vector2f(u + width, v + height),
                new Vector2f(u + width, v)
        };
        NarakaUtils.iterate(positions, uvs, (position, uv) -> {
            vertexConsumer.addVertex(pose, position)
                    .setColor(color)
                    .setLight(packedLight)
                    .setOverlay(packedOverlay)
                    .setUv(uv.x, uv.y)
                    .setNormal(pose, normal.getX(), normal.getY(), normal.getZ());
        });
    }

    @Override
    protected ResourceLocation getTextureLocation(Herobrine entity) {
        return NarakaTextures.HEROBRINE_SCARF;
    }
}

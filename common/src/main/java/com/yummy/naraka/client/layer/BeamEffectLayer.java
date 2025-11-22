package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.renderer.entity.state.BeamEffectRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.BeamEffectRenderStateControl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BeamEffectLayer<T extends Entity & BeamEffectRenderStateControl, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final Vector3fc scale;
    private final Vector3fc offset;

    public BeamEffectLayer(RenderLayerParent<T, M> renderer, Vector3fc scale, Vector3fc offset) {
        super(renderer);
        this.scale = scale;
        this.offset = offset;
    }

    public BeamEffectLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
        this.scale = new Vector3f(1, 1, 1);
        this.offset = new Vector3f(0, 0, 0);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        poseStack.scale(scale.x(), scale.y(), scale.z());
        poseStack.translate(offset.x(), offset.y(), offset.z());
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lightning());
        PoseStack.Pose pose = poseStack.last();
        livingEntity.forEachBeamEffectRenderStates(beamEffectRenderState -> {
            for (BeamEffectRenderState.BeamEffectPart part : beamEffectRenderState.parts) {
                Vec3 position = part.position();
                List<Vector3f> vertices1 = List.of(
                        position.toVector3f().add(0, -0.01f, -0.01f),
                        position.toVector3f().add(0, 0.01f, -0.01f),
                        position.toVector3f().add(0, 0.01f, 0.01f),
                        position.toVector3f().add(0, -0.01f, 0.01f)
                );
                List<Vector3f> vertices2 = List.of(
                        position.toVector3f().add(-0.01f, -0.01f, 0),
                        position.toVector3f().add(-0.01f, 0.01f, 0),
                        position.toVector3f().add(0.01f, 0.01f, 0),
                        position.toVector3f().add(0.01f, -0.01f, 0)
                );

                NarakaRenderUtils.vertices(pose, vertexConsumer, vertices1, NarakaRenderUtils.DEFAULT_UVS, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, part.color(), Direction.UP, false);
                NarakaRenderUtils.vertices(pose, vertexConsumer, vertices1, NarakaRenderUtils.DEFAULT_UVS, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, part.color(), Direction.UP, true);
                NarakaRenderUtils.vertices(pose, vertexConsumer, vertices2, NarakaRenderUtils.DEFAULT_UVS, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, part.color(), Direction.UP, false);
                NarakaRenderUtils.vertices(pose, vertexConsumer, vertices2, NarakaRenderUtils.DEFAULT_UVS, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, part.color(), Direction.UP, true);
            }
        });
        poseStack.popPose();
    }
}

package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.renderer.PurifiedSoulFlameRenderMaterial;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Shadow
    private static void fireVertex(PoseStack.Pose matrixEntry, VertexConsumer buffer, float x, float y, float z, float texU, float texV) {
        throw new AssertionError();
    }

    @Shadow
    private Quaternionf cameraOrientation;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.AFTER))
    private <E extends Entity> void submitNarakaFlame(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity && EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get()) > 0) {
            naraka$renderPurifiedSoulFire(poseStack, buffer, entity, Mth.rotationAroundAxis(Mth.Y_AXIS, cameraOrientation, new Quaternionf()));
        }
    }

    @Unique
    private void naraka$renderPurifiedSoulFire(PoseStack poseStack, MultiBufferSource buffer, Entity entity, Quaternionf quaternion) {
        TextureAtlasSprite textureAtlasSprite = PurifiedSoulFlameRenderMaterial.PURIFIED_SOUL_FIRE_0.sprite();
        TextureAtlasSprite textureAtlasSprite2 = PurifiedSoulFlameRenderMaterial.PURIFIED_SOUL_FIRE_1.sprite();
        poseStack.pushPose();
        float scale = entity.getBbWidth() * 1.4F;
        poseStack.scale(scale, scale, scale);
        float x = 0.5F;
        float i = entity.getBbHeight() / scale;
        float y = 0.0F;
        poseStack.mulPose(quaternion);
        poseStack.translate(0.0F, 0.0F, 0.3F - i * 0.02F);
        float z = 0.0F;
        int l = 0;
        VertexConsumer vertexConsumer = buffer.getBuffer(Sheets.cutoutBlockSheet());

        for (PoseStack.Pose pose = poseStack.last(); i > 0; ++l) {
            TextureAtlasSprite textureAtlasSprite3 = l % 2 == 0 ? textureAtlasSprite : textureAtlasSprite2;
            float u0 = textureAtlasSprite3.getU0();
            float v0 = textureAtlasSprite3.getV0();
            float u1 = textureAtlasSprite3.getU1();
            float v1 = textureAtlasSprite3.getV1();
            if (l / 2 % 2 == 0) {
                float temp = u1;
                u1 = u0;
                u0 = temp;
            }

            fireVertex(pose, vertexConsumer, -x - 0.0F, 0.0F - y, z, u1, v1);
            fireVertex(pose, vertexConsumer, x - 0.0F, 0.0F - y, z, u0, v1);
            fireVertex(pose, vertexConsumer, x - 0.0F, 1.4F - y, z, u0, v0);
            fireVertex(pose, vertexConsumer, -x - 0.0F, 1.4F - y, z, u1, v0);
            i -= 0.45F;
            y -= 0.45F;
            x *= 0.9F;
            z -= 0.03F;
        }

        poseStack.popPose();
    }
}

package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.client.resources.model.Material;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class PurifiedSoulFlameFeatureRenderer {
    public static final Material PURIFIED_SOUL_FIRE_0 = new Material(NarakaTextures.LOCATION_BLOCKS, NarakaMod.identifier("block", "purified_soul_fire_0"));

    public static final Material PURIFIED_SOUL_FIRE_1 = new Material(NarakaTextures.LOCATION_BLOCKS, NarakaMod.identifier("block", "purified_soul_fire_1"));


    public void render(PurifiedSoulFireSubmitNodeCollection purifiedSoulFireSubmitNodeCollection, MultiBufferSource.BufferSource bufferSource, AtlasManager atlasManager) {
        purifiedSoulFireSubmitNodeCollection.naraka$getPurifiedSoulFlameSubmits().forEach(flameSubmit -> {
            renderFlame(flameSubmit.pose(), bufferSource, flameSubmit.entityRenderState(), flameSubmit.rotation(), atlasManager);
        });
    }

    private void renderFlame(
            PoseStack.Pose pose, MultiBufferSource multiBufferSource, EntityRenderState entityRenderState, Quaternionf quaternionf, AtlasManager atlasManager
    ) {
        TextureAtlasSprite purifiedSoulFire0 = atlasManager.get(PURIFIED_SOUL_FIRE_0);
        TextureAtlasSprite purifiedSoulFire1 = atlasManager.get(PURIFIED_SOUL_FIRE_1);
        float scale = entityRenderState.boundingBoxWidth * 1.4F;
        pose.scale(scale, scale, scale);
        float x = 0.5F;
        float zOffset = entityRenderState.boundingBoxHeight / scale;
        float y = 0.0F;
        pose.rotate(quaternionf);
        pose.translate(0.0F, 0.0F, 0.3F - (int) zOffset * 0.02F);
        float z = 0.0F;
        int counter = 0;

        for (VertexConsumer vertexConsumer = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet()); zOffset > 0; counter++) {
            TextureAtlasSprite textureAtlasSprite3 = counter % 2 == 0 ? purifiedSoulFire0 : purifiedSoulFire1;
            float u0 = textureAtlasSprite3.getU0();
            float v0 = textureAtlasSprite3.getV0();
            float u1 = textureAtlasSprite3.getU1();
            float v1 = textureAtlasSprite3.getV1();
            if (counter / 2 % 2 == 0) {
                float temp = u1;
                u1 = u0;
                u0 = temp;
            }

            fireVertex(pose, vertexConsumer, -x, -y, z, u1, v1);
            fireVertex(pose, vertexConsumer, x, -y, z, u0, v1);
            fireVertex(pose, vertexConsumer, x, 1.4F - y, z, u0, v0);
            fireVertex(pose, vertexConsumer, -x, 1.4F - y, z, u1, v0);
            zOffset -= 0.45F;
            y -= 0.45F;
            x *= 0.9F;
            z -= 0.03F;
        }
    }

    private static void fireVertex(PoseStack.Pose pose, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v) {
        vertexConsumer.addVertex(pose, x, y, z)
                .setColor(-1)
                .setUv(u, v)
                .setUv1(0, 10)
                .setLight(240)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}

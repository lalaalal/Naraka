package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class SoulStabilizerBlockEntityRenderer implements BlockEntityRenderer<SoulStabilizerBlockEntity> {
    private static final ResourceLocation WATER_STILL = NarakaMod.mcLocation("textures/block/water_still.png");
    private static final ResourceLocation WATER_OVERLAY = NarakaMod.mcLocation("textures/block/water_overlay.png");

    private final ModelPart bottle;
    private final ModelPart liquid;

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild(
                "bottle",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0,
                                5, 5, 5,
                                new CubeDeformation(0)
                        ),
                PartPose.offset(5.5f, 0, 5.5f)
        );
        partdefinition.addOrReplaceChild(
                "liquid",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0,
                                4, 3.8f, 4,
                                new CubeDeformation(0)
                        ),
                PartPose.offset(6, 1, 6)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public SoulStabilizerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(NarakaModelLayers.SOUL_STABILIZER);
        bottle = root.getChild("bottle");
        liquid = root.getChild("liquid");
    }

    @Override
    public void render(SoulStabilizerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        VertexConsumer bottleBuffer = bufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.SOUL_STABILIZER));
        bottle.render(poseStack, bottleBuffer, packedLight, packedOverlay);
        poseStack.popPose();

        if (blockEntity.getSoulType() == null)
            return;

        float soulsRatio = (float) blockEntity.getSouls() / SoulStabilizerBlockEntity.CAPACITY;
        int color = Color.of(blockEntity.getSoulType().getColor())
                .withAlpha(0x99);

        poseStack.pushPose();
        poseStack.scale(1, soulsRatio, 1);
        VertexConsumer liquidBuffer = bufferSource.getBuffer(RenderType.entityTranslucent(WATER_OVERLAY));
        liquid.render(poseStack, liquidBuffer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }

    private void vertex(VertexConsumer buffer, float x, float y, float z, float red, float green, float blue, float u, float v, int packedLight) {
        buffer.addVertex(x, y, z).setColor(red, green, blue, 1).setUv(u, v).setLight(packedLight).setNormal(0, 1, 0);
    }
}

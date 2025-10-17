package com.yummy.naraka.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;
import com.yummy.naraka.world.item.SoulType;
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
import net.minecraft.util.FastColor;

@Environment(EnvType.CLIENT)
public class SoulStabilizerBlockEntityRenderer implements BlockEntityRenderer<SoulStabilizerBlockEntity> {
    public static final ResourceLocation WATER_OVERLAY = NarakaMod.mcLocation("textures/block/water_overlay.png");

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
        RenderType bottleRenderType = RenderType.entityCutout(NarakaTextures.SOUL_STABILIZER);
        VertexConsumer bottleVertexConsumer = bufferSource.getBuffer(bottleRenderType);
        bottle.render(poseStack, bottleVertexConsumer, packedLight, packedOverlay);
        poseStack.popPose();

        if (blockEntity.getSoulType() == SoulType.NONE)
            return;

        int souls = blockEntity.getSouls();
        float soulRatio = (float) souls / SoulStabilizerBlockEntity.CAPACITY;
        int color = FastColor.ARGB32.color(0x99, blockEntity.getSoulType().getColor());

        poseStack.pushPose();
        poseStack.scale(1, soulRatio, 1);
        RenderType liquidRenderType = RenderType.entityCutout(WATER_OVERLAY);
        VertexConsumer liquidVertexConsumer = bufferSource.getBuffer(liquidRenderType);
        liquid.render(poseStack, liquidVertexConsumer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }
}

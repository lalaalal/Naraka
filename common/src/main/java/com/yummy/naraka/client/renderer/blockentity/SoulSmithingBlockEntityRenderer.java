package com.yummy.naraka.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.world.block.SoulSmithingBlock;
import com.yummy.naraka.world.block.entity.SoulSmithingBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class SoulSmithingBlockEntityRenderer implements BlockEntityRenderer<SoulSmithingBlockEntity> {
    private final ModelPart main;
    private final ModelPart trimTemplate;
    private final ItemRenderer itemRenderer;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public static LayerDefinition createMainLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("main",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(0, 0, 0.0F, 16.0F, 5.0F, 16.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 21).addBox(0, 15, 0.0F, 16.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
                        .texOffs(36, 43).addBox(4, 5, 0.0F, 2.0F, 10.0F, 16.0F, new CubeDeformation(0.0F))
                        .texOffs(56, 38).addBox(0, 5, 10.0F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 59).addBox(13, 10, 0.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 38).addBox(6, 5, 0.0F, 10.0F, 5.0F, 16.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 0).addBox(6, 10, 6.0F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 21).addBox(6, 11, 2.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(7, 9).addBox(12, 11, 2.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(0, 5, 0.0F, 3.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(56, 15).addBox(2, 5, 2.0F, 1.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public static LayerDefinition createTrimTemplateLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("trim_template",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(3, 0, 2,
                                1, 10, 8,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offsetAndRotation(16, 11, 0, 0, 0, Mth.PI)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public SoulSmithingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        main = context.bakeLayer(NarakaModelLayers.SOUL_SMITHING_BLOCK);
        trimTemplate = context.bakeLayer(NarakaModelLayers.TRIM_TEMPLATE);
        itemRenderer = context.itemRenderer();
        blockEntityRenderDispatcher = context.blockEntityRenderDispatcher();
    }

    @Override
    public void render(SoulSmithingBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {
        poseStack.pushPose();
        Direction direction = blockEntity.getBlockState().getValue(SoulSmithingBlock.FACING);
        Quaternionf rotation = Axis.YN.rotationDegrees(direction.toYRot());
        poseStack.rotateAround(rotation, 0.5f, 0.5f, 0.5f);
        poseStack.rotateAround(Axis.ZP.rotation(Mth.PI), 0.5f, 0.5f, 0.5f);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.SOUL_SMITHING_BLOCK));
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        poseStack.popPose();

        renderTrim(blockEntity, poseStack, bufferSource, packedLight, packedOverlay, rotation);
        renderSoulStabilizer(blockEntity, partialTick, poseStack, bufferSource, rotation);
        renderItem(blockEntity, poseStack, bufferSource, packedLight, packedOverlay, rotation);
    }

    private void renderSoulStabilizer(SoulSmithingBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, Quaternionf rotation) {
        if (!blockEntity.isStabilizerAttached())
            return;

        poseStack.pushPose();
        poseStack.rotateAround(rotation, 0.5f, 0.5f, 0.5f);
        poseStack.translate(-1.5f / 16f, 1f / 16f, -4.5f / 16f);
        blockEntityRenderDispatcher.render(blockEntity.getSoulStabilizer(), partialTick, poseStack, bufferSource);
        poseStack.popPose();
    }

    private void renderTrim(SoulSmithingBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, Quaternionf rotation) {
        if (blockEntity.getTemplateItem().isEmpty())
            return;

        poseStack.pushPose();
        poseStack.rotateAround(rotation, 0.5f, 0.5f, 0.5f);
        RenderType renderType = RenderType.entityCutout(NarakaTextures.getTemplateTexture(blockEntity.getTemplateItem()));
        VertexConsumer buffer = bufferSource.getBuffer(renderType);
        trimTemplate.render(poseStack, buffer, light, overlay);
        poseStack.popPose();
    }

    private void renderItem(SoulSmithingBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, Quaternionf rotation) {
        if (blockEntity.getForgingItem().isEmpty())
            return;

        poseStack.pushPose();
        poseStack.rotateAround(rotation.rotateX(Mth.HALF_PI), 0.5f, 0.5f, 0.5f);
        poseStack.translate(0.5, 0.5, 0);
        poseStack.scale(0.8f, 0.8f, 0.8f);
        ItemStack itemStack = blockEntity.getForgingItem();
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, light, overlay, poseStack, bufferSource, blockEntity.getLevel(), 1);
        poseStack.popPose();
    }
}

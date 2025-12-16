package com.yummy.naraka.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.blockentity.state.SoulSmithingBlockRenderState;
import com.yummy.naraka.world.block.SoulSmithingBlock;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.block.entity.SoulSmithingBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class SoulSmithingBlockEntityRenderer implements BlockEntityRenderer<SoulSmithingBlockEntity, SoulSmithingBlockRenderState> {
    private final ModelPart main;
    private final ModelPart trimTemplate;
    private final ItemModelResolver itemModelResolver;
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
        itemModelResolver = context.itemModelResolver();
        blockEntityRenderDispatcher = context.blockEntityRenderDispatcher();
    }

    @Override
    public SoulSmithingBlockRenderState createRenderState() {
        return new SoulSmithingBlockRenderState();
    }

    @Override
    public void extractRenderState(SoulSmithingBlockEntity blockEntity, SoulSmithingBlockRenderState renderState, float f, Vec3 vec3, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, f, vec3, crumblingOverlay);
        this.itemModelResolver.updateForTopItem(renderState.forgingItem, blockEntity.getForgingItem(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, blockEntity.getSouls());
        renderState.direction = blockEntity.getBlockState().getValue(SoulSmithingBlock.FACING);
        renderState.templateItem = blockEntity.getTemplateItem();
        renderState.stabilizerAttached = blockEntity.isStabilizerAttached();
        renderState.stabilizer.lightCoords = renderState.lightCoords;
        renderState.stabilizer.blockEntityType = NarakaBlockEntityTypes.SOUL_STABILIZER.get();
        renderState.stabilizer.souls = blockEntity.getSouls();
        renderState.stabilizer.soulType = blockEntity.getSoulType();
    }

    @Override
    public void submit(SoulSmithingBlockRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        Direction direction = renderState.direction;
        Quaternionf rotation = Axis.YN.rotationDegrees(direction.toYRot());
        poseStack.rotateAround(rotation, 0.5f, 0.5f, 0.5f);
        poseStack.rotateAround(Axis.ZP.rotation(Mth.PI), 0.5f, 0.5f, 0.5f);
        RenderType renderType = RenderType.entityCutout(NarakaTextures.SOUL_SMITHING_BLOCK);
        submitNodeCollector.submitModelPart(main, poseStack, renderType, renderState.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, null);
        poseStack.popPose();

        submitTrim(renderState, poseStack, submitNodeCollector, rotation);
        submitSoulStabilizer(renderState, poseStack, submitNodeCollector, cameraRenderState, rotation);
        submitItem(renderState, poseStack, submitNodeCollector, rotation);
    }

    private void submitSoulStabilizer(SoulSmithingBlockRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, Quaternionf rotation) {
        if (!renderState.stabilizerAttached)
            return;

        poseStack.pushPose();
        poseStack.rotateAround(rotation, 0.5f, 0.5f, 0.5f);
        poseStack.translate(-1.5f / 16f, 1f / 16f, -4.5f / 16f);
        blockEntityRenderDispatcher.submit(renderState.stabilizer, poseStack, submitNodeCollector, cameraRenderState);
        poseStack.popPose();
    }

    private void submitTrim(SoulSmithingBlockRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Quaternionf rotation) {
        if (renderState.templateItem.isEmpty())
            return;

        poseStack.pushPose();
        poseStack.rotateAround(rotation, 0.5f, 0.5f, 0.5f);
        RenderType renderType = RenderType.entityCutout(NarakaTextures.getTemplateTexture(renderState.templateItem));
        submitNodeCollector.submitModelPart(trimTemplate, poseStack, renderType, renderState.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, null);
        poseStack.popPose();
    }

    private void submitItem(SoulSmithingBlockRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Quaternionf rotation) {
        if (renderState.forgingItem.isEmpty())
            return;
        poseStack.pushPose();
        poseStack.rotateAround(rotation.rotateX(Mth.HALF_PI), 0.5f, 0.5f, 0.5f);
        poseStack.translate(0.5, 0.5, 0);
        poseStack.scale(0.8f, 0.8f, 0.8f);
        renderState.forgingItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}

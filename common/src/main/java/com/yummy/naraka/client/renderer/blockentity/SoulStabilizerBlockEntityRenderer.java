package com.yummy.naraka.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.blockentity.state.SoulStabilizerRenderState;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;
import com.yummy.naraka.world.item.SoulType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SoulStabilizerBlockEntityRenderer implements BlockEntityRenderer<SoulStabilizerBlockEntity, SoulStabilizerRenderState> {
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
    public SoulStabilizerRenderState createRenderState() {
        return new SoulStabilizerRenderState();
    }

    @Override
    public void extractRenderState(SoulStabilizerBlockEntity blockEntity, SoulStabilizerRenderState blockEntityRenderState, float f, Vec3 vec3, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, blockEntityRenderState, f, vec3, crumblingOverlay);
        blockEntityRenderState.soulType = blockEntity.getSoulType();
        blockEntityRenderState.souls = blockEntity.getSouls();
    }

    @Override
    public void submit(SoulStabilizerRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        RenderType renderType = RenderType.entityCutout(NarakaTextures.SOUL_STABILIZER);
        submitNodeCollector.submitModelPart(bottle, poseStack, renderType, renderState.lightCoords, OverlayTexture.NO_OVERLAY, null, -1, null);
        poseStack.popPose();

        if (renderState.soulType == SoulType.NONE)
            return;

        float soulRatio = (float) renderState.souls / SoulStabilizerBlockEntity.CAPACITY;
        int color = ARGB.color(0x99, renderState.soulType.getColor());

        poseStack.pushPose();
        poseStack.scale(1, soulRatio, 1);
        submitNodeCollector.submitModelPart(liquid, poseStack, renderType, renderState.lightCoords, OverlayTexture.NO_OVERLAY, null, color, null);
        poseStack.popPose();
    }
}

package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.renderer.entity.state.LightTailEntityRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.CorruptedStar;
import com.yummy.naraka.world.item.SoulType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class CorruptedStarRenderer extends LightTailEntityRenderer<CorruptedStar, LightTailEntityRenderState> {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        root.addOrReplaceChild("inner", CubeListBuilder.create()
                        .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 4.0F, 0.0F)
        );
        root.addOrReplaceChild("outer", CubeListBuilder.create()
                        .addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 4.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    private final ModelPart outer;
    private final ModelPart inner;

    public CorruptedStarRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart root = context.bakeLayer(NarakaModelLayers.CORRUPTED_STAR);
        this.outer = root.getChild("outer");
        this.inner = root.getChild("inner");
    }

    @Override
    public LightTailEntityRenderState createRenderState() {
        return new LightTailEntityRenderState();
    }

    @Override
    public void submit(LightTailEntityRenderState entityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        float rotation = entityRenderState.ageInTicks * 10;
        poseStack.rotateAround(new Quaternionf().setAngleAxis(Mth.PI / 3, NarakaRenderUtils.SIN_45, 0, NarakaRenderUtils.SIN_45), 0, 0.25f, 0);
        poseStack.rotateAround(Axis.YP.rotationDegrees(rotation), 0, 0.25f, 0);
        poseStack.rotateAround(Axis.ZP.rotationDegrees(rotation), 0, 0.25f, 0);
        submitNodeCollector.order(0).submitModelPart(inner, poseStack, RenderTypes.lightning(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, 0xaaffffff, null);
        submitNodeCollector.order(1).submitModelPart(outer, poseStack, RenderTypes.lightning(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, ARGB.color(0xbb, SoulType.COPPER.color), null);
        poseStack.popPose();

        super.submit(entityRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }
}

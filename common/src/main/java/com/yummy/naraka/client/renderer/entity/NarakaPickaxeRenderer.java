package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.model.NarakaPickaxeModel;
import com.yummy.naraka.client.renderer.ColoredItemRenderer;
import com.yummy.naraka.client.renderer.entity.state.NarakaPickaxeRenderState;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.NarakaPickaxe;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class NarakaPickaxeRenderer extends EntityRenderer<NarakaPickaxe, NarakaPickaxeRenderState> {
    private final ItemModelResolver itemModelResolver;
    private final ItemStack pickaxe = NarakaItems.HEROBRINE_PICKAXE.get().getDefaultInstance();
    private final NarakaPickaxeModel model;

    public static void applyTransformAndRotate(PoseStack poseStack, ModelPart part) {
        poseStack.translate(-part.x / 16, -part.y / 16, part.z / 16);
        if (part.xRot != 0 || part.yRot != 0 || part.zRot != 0) {
            poseStack.mulPose(new Quaternionf().rotationZYX(part.zRot, -part.yRot, -part.xRot));
        }
        poseStack.scale(part.xScale, part.yScale, part.zScale);
    }

    public static void applyTransformAndRotate(PoseStack poseStack, ModelPart... parts) {
        for (ModelPart part : parts)
            applyTransformAndRotate(poseStack, part);
    }

    public NarakaPickaxeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new NarakaPickaxeModel(context.bakeLayer(NarakaModelLayers.NARAKA_PICKAXE));
        this.itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public NarakaPickaxeRenderState createRenderState() {
        return new NarakaPickaxeRenderState();
    }

    @Override
    public void extractRenderState(NarakaPickaxe entity, NarakaPickaxeRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.setAnimationVisitor(entity);
        reusedState.yRot = entity.getYRot(partialTick);
        itemModelResolver.updateForNonLiving(reusedState.pickaxe, pickaxe, ItemDisplayContext.NONE, entity);
        ColoredItemRenderer.setTemporaryColorForCurrent(Color.of(0xabffffff));
    }

    @Override
    public void submit(NarakaPickaxeRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        model.setupAnim(renderState);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - renderState.yRot));
        applyTransformAndRotate(poseStack, model.root().getChild("main"));
        poseStack.mulPose(Axis.ZP.rotationDegrees(45));
        poseStack.translate(0.5, 0.5, 0);
        poseStack.scale(4, 4, 1);
        renderState.pickaxe.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
    }

    @Override
    protected AABB getBoundingBoxForCulling(NarakaPickaxe entity) {
        return super.getBoundingBoxForCulling(entity).inflate(4);
    }
}

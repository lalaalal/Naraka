package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.model.NarakaPickaxeModel;
import com.yummy.naraka.world.entity.NarakaPickaxe;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class NarakaPickaxeRenderer extends LivingEntityRenderer<NarakaPickaxe, NarakaPickaxeModel> {
    private final ItemRenderer itemRenderer;
    private final ItemStack pickaxe = NarakaItems.HEROBRINE_PICKAXE.get().getDefaultInstance();

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
        super(context, new NarakaPickaxeModel(context.bakeLayer(NarakaModelLayers.NARAKA_PICKAXE)), 0);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureLocation(NarakaPickaxe entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(NarakaPickaxe entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - entity.getViewYRot(partialTick)));
        applyTransformAndRotate(poseStack, model.root().getChild("main"));
        poseStack.mulPose(Axis.ZP.rotationDegrees(225));
        poseStack.translate(0.5, 0.5, 0);
        poseStack.scale(4, 4, 1);
        itemRenderer.renderStatic(pickaxe, ItemDisplayContext.NONE, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, null, 0);
        poseStack.popPose();
    }
}

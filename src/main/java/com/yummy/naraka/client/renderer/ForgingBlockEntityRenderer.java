package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.world.block.entity.ForgingBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ForgingBlockEntityRenderer implements BlockEntityRenderer<ForgingBlockEntity> {
    private final ModelPart root;
    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public ForgingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        root = context.bakeLayer(NarakaModelLayers.FORGING_BLOCK);
    }

    @Override
    public void render(ForgingBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        poseStack.pushPose();
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.FORGING_BLOCK));
        root.render(poseStack, vertexConsumer, light, overlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.5, 0.7, 0.5);
        ItemStack itemStack = blockEntity.getItemStack();
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND, light, overlay, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
        poseStack.popPose();
    }
}
